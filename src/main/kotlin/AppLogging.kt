package com.eltonkola.desktop

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

object AppLogging {
    private val logFile = File(System.getProperty("user.home"), "compose_app.log")
    private var initialized = false

    fun init() {
        if (initialized) return

        // Create file writer
        val fileWriter = object : LogWriter() {
            private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

            override fun log(
                severity: Severity,
                message: String,
                tag: String,
                throwable: Throwable?
            ) {
                val timestamp = dateFormat.format(Date())
                val entry = buildString {
                    append("[$timestamp] ")
                    append(severity.name.padEnd(7))
                    append("[$tag]: $message")
                    if (throwable != null) {
                        append("\n")
                        append(throwable.stackTraceToString())
                    }
                    append("\n")
                }

                synchronized(this) {
                    logFile.appendText(entry)
                }
            }
        }

        // Configure logger with both console and file output
        Logger.setLogWriters(
            CommonWriter(), // Console output
            fileWriter      // File output
        )

        // Set up crash handler
        setupCrashHandler()

        initialized = true
        Logger.i("Logging initialized")
    }

    private fun setupCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            // Log to crash-specific file
            val crashFile = File(System.getProperty("user.home"), "app-crash.log")
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())

            val crashLog = """
                |${"=".repeat(80)}
                |CRASH REPORT - $timestamp
                |Thread: ${thread.name}
                |Exception: ${throwable::class.java.name}
                |Message: ${throwable.message}
                |
                |Stack Trace:
                |${throwable.stackTraceToString()}
                |
                |System Info:
                |  Java: ${System.getProperty("java.version")}
                |  OS: ${System.getProperty("os.name")} ${System.getProperty("os.version")}
                |  User: ${System.getProperty("user.name")}
                |${"=".repeat(80)}
                |
            """.trimMargin()

            crashFile.appendText(crashLog)

            // Also log via Kermit
            Logger.e("Uncaught exception", throwable, "Crash")
        }
    }
}
