package com.eltonkola.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eltonkola.desktop.desktopapp.generated.resources.Res
import com.eltonkola.desktop.desktopapp.generated.resources.desktop_computer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.CircularProgressIndicator
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Divider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.foundation.util.JewelLogger
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.intui.window.styling.light
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.styling.DecoratedWindowStyle
import org.jetbrains.jewel.window.styling.TitleBarStyle
import org.jetbrains.skiko.SystemTheme
import org.jetbrains.skiko.currentSystemTheme

// Data models
data class Message(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: String,
    val isThinking: Boolean = false
)

data class Chat(
    val id: String,
    val title: String,
    val lastMessage: String,
    val timestamp: String
)

// Data classes remain the same

@Composable
fun ChatScreen() {
    var chats by remember { mutableStateOf(emptyList<Chat>()) }
    var selectedChatId by remember { mutableStateOf<String?>(null) }
    
    // Add a default welcome chat if no chats exist
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
                            if (chat.id == selectedChatId) chat.copy(title = newTitle)
                            else chat
                        }
                    }
                )
            }
        } else {
            // Show empty state or new chat prompt
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Select a chat or create a new one")
            }
        }
    }
}

@Composable
fun ChatSidebar(
    chats: List<Chat>,
    selectedChatId: String?,
    onChatSelected: (String) -> Unit,
    onNewChat: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(JewelTheme.globalColors.panelBackground)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "ChatGPT 4.0",
                fontWeight = FontWeight.Bold
            )
            
            DefaultButton(
                onClick = onNewChat,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(AllIconsKeys.General.Add, contentDescription = "New Chat")
            }
        }
        
        Divider(
            color = JewelTheme.globalColors.borders.normal,
            orientation = Orientation.Horizontal
        )
        
        // Chat list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            items(chats) { chat ->
                ChatListItem(
                    chat = chat,
                    isSelected = chat.id == selectedChatId,
                    onClick = { onChatSelected(chat.id) }
                )
            }
        }

        // User info
        Divider(
            color = JewelTheme.globalColors.borders.normal,
            orientation = Orientation.Horizontal
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(JewelTheme.globalColors.panelBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "JD",
                    style = JewelTheme.defaultTextStyle.copy(color = JewelTheme.globalColors.text.normal)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "John Developer",
                    style = JewelTheme.defaultTextStyle.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "Free Plan",
//                    style = JewelTheme.defaultTextStyle.small,
//                    color = JewelTheme.globalColors.colors.content.muted
                )
            }
        }
    }
}

@Composable
fun ChatListItem(chat: Chat, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) JewelTheme.globalColors.text.normal.copy(alpha = 0.2f)
        else JewelTheme.globalColors.panelBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                AllIconsKeys.General.Balloon,
                contentDescription = null,
                tint = JewelTheme.globalColors.text.disabled,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    chat.title,
                    maxLines = 1,
                    style = JewelTheme.defaultTextStyle.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    chat.lastMessage,
                    maxLines = 1,
//                    style = JewelTheme.defaultTextStyle.small,
                    color = JewelTheme.globalColors.text.disabledSelected
                )

                Text(
                    chat.timestamp,
//                    style = JewelTheme.defaultTextStyle.extraSmall,
                    color = JewelTheme.globalColors.text.disabled,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun MainChatArea(
    chat: Chat,
    onUpdateChatTitle: (String) -> Unit = {}
) {
    var messages by remember(chat.id) { 
        mutableStateOf(emptyList<Message>()) 
    }
    var userInput by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JewelTheme.globalColors.panelBackground)
    ) {
        // Chat header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "ChatGPT",
//                style = JewelTheme.defaultTextStyle.fontStyle,
                color = JewelTheme.globalColors.text.normal
            )


                DefaultButton(
                    onClick = { /* Regenerate */ },
                    enabled = !isGenerating
                ) {
                    Row {
                        Icon(AllIconsKeys.Javaee.UpdateRunningApplication, contentDescription = "Regenerate")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Regenerate")
                    }
                }
            }

        Divider(
            color = JewelTheme.globalColors.borders.normal,
            orientation = Orientation.Horizontal
        )

        // Messages area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message = message)

                    if (message.isThinking) {
                        ThinkingIndicator()
                    }
                }
            }

            // Auto-scroll to bottom
            LaunchedEffect(messages.size) {
                if (messages.isNotEmpty()) {
                    lazyListState.animateScrollToItem(messages.size - 1)
                }
            }
        }

        // Input area
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = JewelTheme.globalColors.panelBackground,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Suggestions
                if (messages.size <= 2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "Explain quantum computing",
                            "Write a poem about AI",
                            "Plan a week-long trip to Paris",
                            "Debug this Kotlin code"
                        ).forEach { suggestion ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                             //   color = JewelTheme.globalColors.text.normal.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = suggestion,
                                  //  style = JewelTheme.defaultTextStyle.fontStyle,
                                    modifier = Modifier
                                        .clickable {
                                            userInput = suggestion
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Input field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val userInput = rememberTextFieldState("")

//                    LaunchedEffect(state){
//                        userInput = state.text.toString()
//                    }

//                    OutlinedTextField(
//                        value = userInput,
//                        onValueChange = { userInput = it },
//                        placeholder = { Text("Message ChatGPT...") },
//                        modifier = Modifier.weight(1f),
//                        enabled = !isGenerating,
//                        maxLines = 5
//                    )

                    TextArea(
                        state = userInput,
                        placeholder = {  Text("Message ChatGPT...") },
                        modifier = Modifier.weight(1f).height(80.dp),
                        readOnly = isGenerating,

//                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    val coroutineScope = rememberCoroutineScope()
                    
                    IconButton(
                        onClick = {
                            if (userInput.text.isNotBlank() && !isGenerating) {
                                val userMessage = userInput.text.toString()
                                // If this is the first message, update the chat title
                                if (messages.isEmpty()) {
                                    onUpdateChatTitle(userMessage.take(30).let { if (it.length < userMessage.length) "$it..." else it })
                                }
                                sendMessage(userMessage, messages, { messages = it }, { isGenerating = it }, coroutineScope)
                                userInput.setTextAndPlaceCursorAtEnd("")
                            }
                        },
                        enabled = userInput.text.isNotBlank() && !isGenerating,
                        modifier = Modifier.size(48.dp)
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                               // strokeWidth = 2.dp
                            )
                        } else {
                            Icon(AllIconsKeys.General.ArrowRight, contentDescription = "Send")
                        }
                    }
                }

                // Disclaimer
                Text(
                    "ChatGPT can make mistakes. Consider checking important information.",
//                    style = JewelTheme.defaultTextStyle.extraSmall,
                 //   color = JewelTheme.globalColors.text.disabled,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isUser) {
                JewelTheme.globalColors.borders.normal.copy(alpha = 0.2f)
            } else {
                JewelTheme.globalColors.borders.normal.copy(alpha = 0.4f)
            }
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .padding(16.dp)
            ) {
                // Avatar and header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isUser)  JewelTheme.globalColors.text.normal
                                else   JewelTheme.globalColors.text.warning
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isUser) {
                            Icon(AllIconsKeys.General.User, contentDescription = "User", modifier = Modifier.size(16.dp))
                        } else {
                            Text(
                                "AI",
//                                style = JewelTheme.defaultTextStyle.extraSmall.copy(
//                                    color = JewelTheme.globalColors.colors.secondary.background.normal
//                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isUser) "You" else "ChatGPT",
                     //   style = JewelTheme.defaultTextStyle.fontStyle.copy(fontWeight = FontWeight.Bold),
                        color = if (isUser) JewelTheme.globalColors.text.normal
                        else JewelTheme.globalColors.text.selected
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        message.timestamp,
                       // style = JewelTheme.defaultTextStyle.fontStyle,
                        color = if (isUser) JewelTheme.globalColors.text.normal.copy(alpha = 0.8f)
                        else JewelTheme.globalColors.text.selected
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Message content
                Text(
                    message.text,
                    style = JewelTheme.defaultTextStyle,
                    color = if (isUser) JewelTheme.globalColors.text.warning
                    else JewelTheme.globalColors.text.normal
                )

                // Actions (for AI messages)
                if (!isUser && !message.isThinking) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { /* Copy */ },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(AllIconsKeys.General.Copy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                        }

                        IconButton(
                            onClick = { /* Like */ },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(AllIconsKeys.Nodes.Favorite, contentDescription = "Like", modifier = Modifier.size(16.dp))
                        }

                        IconButton(
                            onClick = { /* Dislike */ },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(AllIconsKeys.Nodes.Bookmark, contentDescription = "Dislike", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThinkingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface (
            shape = RoundedCornerShape(16.dp),
            color = JewelTheme.globalColors.panelBackground
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
//                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Thinking...",
                    style = JewelTheme.defaultTextStyle,
//                    color = JewelTheme.globalColors.colors.content.normal
                )
            }
        }
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

    val updatedMessages = currentMessages + userMessage + thinkingMessage
    updateMessages(updatedMessages)
    updateGenerating(true)

    coroutineScope.launch {
        val result = AiClient.chat(text)
        
        result.fold(
            onSuccess = { aiResponse ->
                val aiMessage = Message(
                    id = (updatedMessages.size + 1).toString(),
                    text = aiResponse.content,
                    isUser = false,
                    timestamp = "Just now"
                )
                updateMessages(
                    updatedMessages.filterNot { it.isThinking } + aiMessage
                )
            },
            onFailure = { error ->
                val errorMessage = Message(
                    id = (updatedMessages.size + 1).toString(),
                    text = "Error: ${error.message ?: "Failed to get response"}",
                    isUser = false,
                    timestamp = "Just now"
                )
                updateMessages(
                    updatedMessages.filterNot { it.isThinking } + errorMessage
                )
            }
        )
        
        updateGenerating(false)
    }
}
