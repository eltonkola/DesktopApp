package com.eltonkola.desktop.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

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
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(16.dp),
                        clip = true,
                        spotColor = Color.Black.copy(alpha = 0.1f),
                        ambientColor = Color.Black.copy(alpha = 0.05f)
                    )
            ) {
                Column(
                    modifier = Modifier .padding(16.dp)
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
