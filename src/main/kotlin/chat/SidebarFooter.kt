package com.eltonkola.desktop.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eltonkola.desktop.desktopapp.generated.resources.Res
import com.eltonkola.desktop.desktopapp.generated.resources.elton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import java.awt.Desktop
import java.net.URI

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
                Image(painter = painterResource(Res.drawable.elton), contentDescription = null)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Elton Kola",
                    style = JewelTheme.defaultTextStyle.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    "Software Engineer",
                    style = JewelTheme.defaultTextStyle.copy(
                        fontSize = 12.sp,
                        color = JewelTheme.globalColors.text.disabled
                    )
                )
            }

            IconButton(
                onClick =   { Desktop.getDesktop().browse(URI.create("https://github.com/eltonkola")) },
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
