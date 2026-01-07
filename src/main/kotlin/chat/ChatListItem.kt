package com.eltonkola.desktop.chat

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun ChatListItem(
    chat: Chat, 
    isAlone: Boolean,
    isSelected: Boolean, 
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
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
            
            if (isSelected && onDelete != null && !isAlone) {
                IconButton(
                    onClick = { onDelete() },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        AllIconsKeys.General.Delete,
                        contentDescription = "Delete Chat",
                        tint = JewelTheme.globalColors.text.error
                    )
                }
            }
        }
    }
}
