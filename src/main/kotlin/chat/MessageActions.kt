package com.eltonkola.desktop.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.icons.AllIconsKeys

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
