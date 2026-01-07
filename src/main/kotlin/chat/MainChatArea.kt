package com.eltonkola.desktop.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eltonkola.desktop.chat.data.AiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider

@Composable
fun MainChatArea(
    chat: Chat,
    onUpdateChatTitle: (String) -> Unit = {}
) {
    var messages by remember(chat.id) { mutableStateOf(emptyList<Message>()) }
    var isGenerating by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JewelTheme.globalColors.panelBackground)
    ) {
        // Chat header
        ChatHeader()

        Divider(
            color = JewelTheme.globalColors.borders.normal,
            orientation = Orientation.Horizontal
        )

        // Messages area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (messages.isEmpty()) {
                WelcomeView()
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(24.dp),
                    reverseLayout = true,
                ) {
                    items(messages.reversed()) { message ->
                        MessageBubble(message = message)
                    }
                }

                LaunchedEffect(messages) {
                    if (messages.isNotEmpty()) {
                        // Scroll to the last item (most recent message)
                        lazyListState.scrollToItem(0)
                    }
                }
            }
        }

        // Input area
        InputArea(
            showSuggestions = messages.isEmpty(),
            isGenerating = isGenerating,
            onSend = { text ->
                if (messages.isEmpty()) {
                    onUpdateChatTitle(text.take(40))
                }
                sendMessage(
                    text = text,
                    currentMessages = messages,
                    updateMessages = { messages = it },
                    updateGenerating = { isGenerating = it },
                    coroutineScope = coroutineScope
                )
            }
        )
    }
}


fun sendMessage(
    text: String,
    currentMessages: List<Message>,
    updateMessages: (List<Message>) -> Unit,
    updateGenerating: (Boolean) -> Unit,
    coroutineScope: CoroutineScope
) {
    val userMessage = Message(
        id = (currentMessages.size + 1).toString(),
        text = text,
        isUser = true,
        timestamp = "Just now"
    )

    val thinkingMessage = Message(
        id = (currentMessages.size + 2).toString(),
        text = "",
        isUser = false,
        timestamp = "Just now",
        isThinking = true
    )

    updateMessages(currentMessages + userMessage + thinkingMessage)
    updateGenerating(true)

    coroutineScope.launch {
        val result = AiClient.chat(text)

        result.fold(
            onSuccess = { aiResponse ->
                val aiMessage = Message(
                    id = (currentMessages.size + 3).toString(),
                    text = aiResponse.content,
                    isUser = false,
                    timestamp = "Just now"
                )
                updateMessages(
                    (currentMessages + userMessage).filterNot { it.isThinking } + aiMessage
                )
            },
            onFailure = { error ->
                val errorMessage = Message(
                    id = (currentMessages.size + 3).toString(),
                    text = "❌ Error: ${error.message ?: "Failed to get response"}",
                    isUser = false,
                    timestamp = "Just now"
                )
                updateMessages(
                    (currentMessages + userMessage).filterNot { it.isThinking } + errorMessage
                )
            }
        )

        updateGenerating(false)
    }
}
