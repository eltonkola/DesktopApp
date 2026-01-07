package com.eltonkola.desktop

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.*
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.window.DecoratedWindowScope
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.newFullscreenControls
import java.awt.Desktop
import java.net.URI

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalLayoutApi
@Composable
fun DecoratedWindowScope.TitleBarView(
    darkTheme: Boolean,
    panelOpened: Boolean,
    switchTheme:() -> Unit,
    onSidebarClick:() -> Unit
) {
    TitleBar(
        Modifier.newFullscreenControls(),
        gradientStartColor = if (darkTheme) Color.DarkGray else Color.Gray

    ) {

        Row(
            Modifier.align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
            ) {
            
            IconButton(onSidebarClick) {
                Icon(
                    imageVector = if(panelOpened) Lucide.PanelRightClose else Lucide.PanelRightOpen,
                    contentDescription = "Sidebar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Text(title)
        }
        
        Row(Modifier.align(Alignment.End)) {
            Tooltip({ Text("Open Jewel Github repository") }) {
                IconButton(
                    { Desktop.getDesktop().browse(URI.create("https://github.com/eltonkola/DesktopApp")) },
                    Modifier.size(40.dp).padding(5.dp),
                ) {
                    Icon(
                        imageVector = Lucide.Github,
                        contentDescription = "Github",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            }

            Tooltip(
                tooltip = {
                    if (darkTheme) {
                        Text("Switch to dark theme")
                    } else {
                        Text("Switch to light theme")
                    }
                }
            ) {
                IconButton(
                    onClick = { 
                        switchTheme()
                    },
                    modifier = Modifier.size(40.dp).padding(5.dp)
                ) {
                    if (!darkTheme) {
                        Icon(
                            imageVector = Lucide.Sun,
                            contentDescription = "Light",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Lucide.Moon,
                            contentDescription = "Dark",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )

                    }
                }
            }
        }
    }
}
