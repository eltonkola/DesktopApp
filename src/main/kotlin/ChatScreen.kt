package com.eltonkola.desktop

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eltonkola.desktop.desktopapp.generated.resources.Res
import com.eltonkola.desktop.desktopapp.generated.resources.desktop_computer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.icons.AllIconsKeys

// ============================================================================
// DATA MODELS
// ============================================================================

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

// ============================================================================
// MAIN SCREEN
// ============================================================================

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

@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(JewelTheme.globalColors.borders.normal.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    AllIconsKeys.General.Balloon,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = JewelTheme.globalColors.text.disabled
                )
            }
            Text(
                "No chat selected",
                style = JewelTheme.defaultTextStyle.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                "Select a chat or create a new one to get started",
                color = JewelTheme.globalColors.text.disabled
            )
        }
    }
}

// ============================================================================
// SIDEBAR
// ============================================================================

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
            .background(JewelTheme.globalColors.panelBackground.copy(
                red = (JewelTheme.globalColors.panelBackground.red * 0.9f),
                green = (JewelTheme.globalColors.panelBackground.green * 0.9f),
                blue = (JewelTheme.globalColors.panelBackground.blue * 0.9f)
            ))
    ) {
        // Header with New Chat button
        SidebarHeader(onNewChat)

        Divider(
            color = JewelTheme.globalColors.borders.normal,
            orientation = Orientation.Horizontal
        )

        // Chat list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(chats) { chat ->
                ChatListItem(
                    chat = chat,
                    isSelected = chat.id == selectedChatId,
                    onClick = { onChatSelected(chat.id) }
                )
            }
        }

        // User profile footer
        SidebarFooter()
    }
}

@Composable
fun SidebarHeader(onNewChat: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                AllIconsKeys.General.Balloon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = JewelTheme.globalColors.text.info
            )
            Text(
                "Chats",
                style = JewelTheme.defaultTextStyle.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        IconButton(
            onClick = onNewChat,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                AllIconsKeys.General.Add,
                contentDescription = "New Chat",
                tint = JewelTheme.globalColors.text.normal
            )
        }
    }
}

@Composable
fun SidebarFooter() {
    Column {
        Divider(
            color = JewelTheme.globalColors.borders.normal,
            orientation = Orientation.Horizontal
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF667EEA),
                                Color(0xFF764BA2)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "JD",
                    style = JewelTheme.defaultTextStyle.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "John Developer",
                    style = JewelTheme.defaultTextStyle.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    "Free Plan",
                    style = JewelTheme.defaultTextStyle.copy(
                        fontSize = 12.sp,
                        color = JewelTheme.globalColors.text.disabled
                    )
                )
            }

            IconButton(
                onClick = { /* Settings */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    AllIconsKeys.General.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ChatListItem(chat: Chat, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected)
            JewelTheme.globalColors.text.normal.copy(alpha = 0.12f)
        else
            Color.Transparent,
        animationSpec = tween(200)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                AllIconsKeys.General.Balloon,
                contentDescription = null,
                tint = if (isSelected)
                    JewelTheme.globalColors.text.normal
                else
                    JewelTheme.globalColors.text.disabled,
                modifier = Modifier.size(18.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    chat.title,
                    maxLines = 1,
                    style = JewelTheme.defaultTextStyle.copy(
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )

                if (chat.lastMessage.isNotEmpty()) {
                    Text(
                        chat.lastMessage,
                        maxLines = 1,
                        style = JewelTheme.defaultTextStyle.copy(
                            fontSize = 12.sp,
                            color = JewelTheme.globalColors.text.disabled
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

// ============================================================================
// MAIN CHAT AREA
// ============================================================================

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
                    contentPadding = PaddingValues(24.dp)
                ) {
                    items(messages) { message ->
                        MessageBubble(message = message)
                    }
                }

                LaunchedEffect(messages.size) {
                    if (messages.isNotEmpty()) {
                        delay(100)
                        lazyListState.animateScrollToItem(messages.size - 1)
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

@Composable
fun ChatHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painterResource(Res.drawable.desktop_computer),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = JewelTheme.globalColors.text.info
            )

            Text(
                "DesktopAi",
                style = JewelTheme.defaultTextStyle.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun WelcomeView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF667EEA),
                            Color(0xFF764BA2)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(Res.drawable.desktop_computer),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "How can I help you today?",
            style = JewelTheme.defaultTextStyle.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Ask me anything or pick a suggestion below",
            style = JewelTheme.defaultTextStyle.copy(
                fontSize = 14.sp,
                color = JewelTheme.globalColors.text.disabled
            )
        )
    }
}

@Composable
fun InputArea(
    showSuggestions: Boolean,
    isGenerating: Boolean,
    onSend: (String) -> Unit
) {
    val textState = rememberTextFieldState("")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = JewelTheme.globalColors.panelBackground,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Suggestions
            if (showSuggestions) {
                SuggestionChips(onSuggestionClick = { onSend(it) })
            }

            // Input field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(2.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    color = JewelTheme.globalColors.panelBackground
                ) {
                    TextArea(
                        state = textState,
                        placeholder = { Text("Message AI Assistant...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp, max = 120.dp)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        readOnly = isGenerating
                    )
                }

                IconButton(
                    onClick = {
                        if (textState.text.isNotBlank() && !isGenerating) {
                            onSend(textState.text.toString())
                            textState.setTextAndPlaceCursorAtEnd("")
                        }
                    },
                    enabled = textState.text.isNotBlank() && !isGenerating,
                    modifier = Modifier.size(48.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Icon(
                            AllIconsKeys.General.ArrowRight,
                            contentDescription = "Send",
                            tint = if (textState.text.isNotBlank())
                                JewelTheme.globalColors.text.info
                            else
                                JewelTheme.globalColors.text.disabled
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionChips(onSuggestionClick: (String) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            "Explain quantum computing",
            "Write a creative story",
            "Help me debug code"
        ).forEach { suggestion ->
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = JewelTheme.globalColors.text.normal.copy(alpha = 0.08f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSuggestionClick(suggestion) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            AllIconsKeys.General.Tip,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = JewelTheme.globalColors.text.normal
                        )
                        Text(
                            text = suggestion,
                            style = JewelTheme.defaultTextStyle.copy(fontSize = 13.sp)
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// MESSAGE COMPONENTS
// ============================================================================

@Composable
fun MessageBubble(message: Message) {
    val isUser = message.isUser

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically { it / 2 }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!isUser) {
                Spacer(modifier = Modifier.width(8.dp))
            }

            val bubbleColor = if (isUser) {
                JewelTheme.globalColors.text.info.copy(alpha = 0.15f)
            } else {
                JewelTheme.globalColors.borders.normal.copy(alpha = 0.15f)
            }

            Surface(
                shape = RoundedCornerShape(
                    topStart = if (isUser) 16.dp else 4.dp,
                    topEnd = if (isUser) 4.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                color = bubbleColor,
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .shadow(1.dp, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).background(bubbleColor)
                ) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isUser) {
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF667EEA),
                                                Color(0xFF764BA2)
                                            )
                                        )
                                    } else {
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFf093fb),
                                                Color(0xFFf5576c)
                                            )
                                        )
                                    }
                                )
                            ,
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isUser) AllIconsKeys.General.User else AllIconsKeys.Toolwindows.ToolWindowRun,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                        }

                        Text(
                            if (isUser) "You" else "AI Assistant",
                            style = JewelTheme.defaultTextStyle.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = JewelTheme.globalColors.text.normal
                            )
                        )

                        Text(
                            message.timestamp,
                            style = JewelTheme.defaultTextStyle.copy(
                                fontSize = 11.sp,
                                color = JewelTheme.globalColors.text.disabled
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Content
                    if (message.isThinking) {
                        ThinkingIndicator()
                    } else {
                        Text(
                            message.text,
                            style = JewelTheme.defaultTextStyle.copy(
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = JewelTheme.globalColors.text.normal
                            )
                        )

                        // Actions for AI messages
                        if (!isUser) {
                            Spacer(modifier = Modifier.height(12.dp))
                            MessageActions()
                        }
                    }
                }
            }

            if (isUser) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun ThinkingIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp)
        )
        Text(
            "Thinking...",
            style = JewelTheme.defaultTextStyle.copy(
                fontSize = 14.sp,
                color = JewelTheme.globalColors.text.disabled
            )
        )
    }
}
@Composable
fun MessageActions() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(
            onClick = { /* Copy */ },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                AllIconsKeys.General.Copy,
                contentDescription = "Copy",
                modifier = Modifier.size(16.dp),
                tint = JewelTheme.globalColors.text.disabled
            )
        }

        IconButton(
            onClick = { /* Like */ },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                AllIconsKeys.Ide.Like,  // Changed
                contentDescription = "Like",
                modifier = Modifier.size(16.dp),
                tint = JewelTheme.globalColors.text.disabled
            )
        }

        IconButton(
            onClick = { /* Dislike */ },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                AllIconsKeys.Ide.Dislike,  // Changed
                contentDescription = "Dislike",
                modifier = Modifier.size(16.dp),
                tint = JewelTheme.globalColors.text.disabled
            )
        }
    }
}

// ============================================================================
// BUSINESS LOGIC
// ============================================================================

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
