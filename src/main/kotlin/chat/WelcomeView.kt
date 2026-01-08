package com.eltonkola.desktop.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eltonkola.desktop.desktopapp.generated.resources.Res
import com.eltonkola.desktop.desktopapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun WelcomeView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var showDebug by remember { mutableStateOf(false) }


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
                painterResource(Res.drawable.logo),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
                    .clickable{
                        showDebug = !showDebug
                    }

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

        AnimatedVisibility(showDebug) {
            DebugModuleList()
        }

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
fun DebugModuleList(){
    LazyColumn(
        modifier = Modifier.size(200.dp, 200.dp)
    ) {
        ModuleLayer.boot().modules().forEach { module ->
            item {
                Text(
                    "Module : ${module.name}",
                    style = JewelTheme.defaultTextStyle.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nr of modules: ${ModuleLayer.boot().modules().size}",
                    modifier = Modifier.weight(1f)
                )
                IconButton({
                    ModuleLayer.boot().modules().joinToString(",") { "\"${it.name}\"\n" }.copyToClipboard()
                }){
                    org.jetbrains.jewel.ui.component.Icon(AllIconsKeys.General.Copy, null)
                }
            }

        }
    }
}

fun String.copyToClipboard() {
    java.awt.Toolkit.getDefaultToolkit().systemClipboard.setContents(
        java.awt.datatransfer.StringSelection(this),
        null
    )
}
