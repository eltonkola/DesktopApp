package com.eltonkola.desktop.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.eltonkola.desktop.chat.data.AiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider


@Composable
fun ChatScreen() {
    var chats by remember { mutableStateOf(emptyList<Chat>()) }
    var selectedChatId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (chats.isEmpty()) {
            val welcomeChat = Chat(
                id = "1",
                title = "New Chat",
                lastMessage = "",
                timestamp = "Just now"
            )
            chats = listOf(welcomeChat)
            selectedChatId = welcomeChat.id
        }
    }

    val onNewChat = {
        val newChat = Chat(
            id = (chats.size + 1).toString(),
            title = "New Chat ${chats.size + 1}",
            lastMessage = "",
            timestamp = "Just now"
        )
        chats = chats + newChat
        selectedChatId = newChat.id
    }

    Row(modifier = Modifier.fillMaxSize()) {
        ChatSidebar(
            chats = chats,
            selectedChatId = selectedChatId,
            onChatSelected = { chatId -> selectedChatId = chatId },
            onNewChat = onNewChat
        )

        Divider(
            color = JewelTheme.globalColors.borders.normal,
            orientation = Orientation.Vertical
        )

        if (selectedChatId != null) {
            val selectedChat = chats.find { it.id == selectedChatId }
            if (selectedChat != null) {
                MainChatArea(
                    chat = selectedChat,
                    onUpdateChatTitle = { newTitle ->
                        chats = chats.map { chat ->
                            if (chat.id == selectedChatId) {
                                chat.copy(
                                    title = newTitle,
                                    lastMessage = if (newTitle.length > 50) "${newTitle.take(50)}..." else newTitle
                                )
                            } else chat
                        }
                    }
                )
            }
        } else {
            EmptyStateView()
        }
    }
}

