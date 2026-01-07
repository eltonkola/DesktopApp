package com.eltonkola.desktop.chat.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicLong

/**
 * Client for interacting with FreeLLM API
 */
object AiClient {
    private const val BASE_URL = "https://apifreellm.com"
    private const val RATE_LIMIT_MILLIS = 5000L

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        install(HttpTimeout) {
            socketTimeoutMillis = 300000
            connectTimeoutMillis = 100000
            requestTimeoutMillis = 300000
        }

        install(HttpRequestRetry) {
            maxRetries = 3
            exponentialDelay()
        }

        // Remove HttpResponseValidator or modify it to not throw on 400
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                // Don't throw for client errors, let the response be parsed
                if (exception !is ClientRequestException) {
                    throw exception
                }
            }
        }
    }

    private val lastRequestTime = AtomicLong(0)

    @Serializable
    data class ApiRequest(
        val message: String
    )

    @Serializable
    data class ApiResponse(
        val response: String? = null,
        val error: String? = null,
        val status: String
    )

    /**
     * Send a chat message to the AI
     */
    suspend fun chat(
        message: String
    ): Result<AiResponse> {
        return try {
            // Enforce rate limiting - FIXED: Only wait if needed
            enforceRateLimit()

            val response: HttpResponse = httpClient.post("$BASE_URL/api/chat") {
                contentType(ContentType.Application.Json)
                setBody(ApiRequest(message = message))
            }

            // Update last call time immediately after successful request
            lastRequestTime.set(System.currentTimeMillis())

            // Parse response based on status code
            if (response.status.isSuccess()) {
                try {
                    val responseBody = response.body<ApiResponse>()
                    Result.success(
                        AiResponse(
                            content = responseBody.response ?: "",
                            isSuccess = responseBody.status == "success",
                            error = responseBody.error,
                            rawResponse = responseBody
                        )
                    )
                } catch (e: Exception) {
                    Result.success(
                        AiResponse(
                            content = "Error parsing response: ${e.message}",
                            isSuccess = false,
                            error = "Parse error"
                        )
                    )
                }
            } else {
                // Handle HTTP errors
                val errorText = response.bodyAsText()
                Result.success(
                    AiResponse(
                        content = "HTTP ${response.status.value}: $errorText",
                        isSuccess = false,
                        error = "HTTP error"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Get time until next allowed request
     */
    fun getTimeUntilNextRequest(): Long {
        val now = System.currentTimeMillis()
        val lastTime = lastRequestTime.get()
        val elapsed = now - lastTime

        // If no previous request or enough time has passed, no wait needed
        if (lastTime == 0L || elapsed >= RATE_LIMIT_MILLIS) {
            return 0
        }

        // Return remaining time to wait
        return RATE_LIMIT_MILLIS - elapsed
    }

    /**
     * Check if we can make a request now (for UI purposes)
     */
    fun canMakeRequest(): Boolean {
        return getTimeUntilNextRequest() == 0L
    }

    /**
     * Close the HTTP client
     */
    fun close() {
        httpClient.close()
    }

    private suspend fun enforceRateLimit() {
        val waitTime = getTimeUntilNextRequest()
        if (waitTime > 0) {
            println("[AiClient] Rate limiting: waiting ${waitTime}ms (last call was ${System.currentTimeMillis() - lastRequestTime.get()}ms ago)")
            delay(waitTime)
        } else {
            println("[AiClient] No rate limit wait needed (last call was ${if (lastRequestTime.get() == 0L) "never" else "${System.currentTimeMillis() - lastRequestTime.get()}ms ago"})")
        }
    }

    /**
     * Debug method to check timing
     */
    fun debugTiming() {
        val now = System.currentTimeMillis()
        val last = lastRequestTime.get()
        println("""
            [DEBUG]
            Current time: $now
            Last request: ${if (last == 0L) "never" else last}
            Time since last: ${if (last == 0L) "N/A" else now - last}ms
            Need to wait: ${getTimeUntilNextRequest()}ms
            Can make request: ${canMakeRequest()}
        """.trimIndent())
    }
}

/**
 * User-friendly AI response wrapper
 */
data class AiResponse(
    val content: String,
    val isSuccess: Boolean,
    val error: String? = null,
    val rawResponse: Any? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    val isError: Boolean get() = !isSuccess
}

// Extension functions for convenience
suspend fun AiClient.ask(question: String): String? {
    return chat(question).getOrNull()?.takeIf { it.isSuccess }?.content
}
