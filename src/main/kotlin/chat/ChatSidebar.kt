package com.eltonkola.desktop.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider

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
