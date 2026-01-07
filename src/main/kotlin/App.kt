package com.eltonkola.desktop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.eltonkola.desktop.chat.ChatScreen
import com.eltonkola.desktop.desktopapp.generated.resources.Res
import com.eltonkola.desktop.desktopapp.generated.resources.desktop_computer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.foundation.util.JewelLogger
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.intui.window.styling.light
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.styling.DecoratedWindowStyle
import org.jetbrains.jewel.window.styling.TitleBarStyle
import org.jetbrains.skiko.SystemTheme
import org.jetbrains.skiko.currentSystemTheme

@Composable
fun App(
    exitApplication: () -> Unit
) {
    
        JewelLogger.getInstance("StandaloneSample").info("Starting Jewel Standalone sample")

        var darkTheme by mutableStateOf(currentSystemTheme == SystemTheme.DARK)

        IntUiTheme(
            theme = if (darkTheme) {
                JewelTheme.darkThemeDefinition()
            } else {
                JewelTheme.lightThemeDefinition()
            },
            styling =
                ComponentStyling.default()
                    .decoratedWindow(
                        titleBarStyle =
                            if (darkTheme) {
                                TitleBarStyle.light()
                            } else {
                                TitleBarStyle.dark()
                            },
                        windowStyle = if (darkTheme) {
                            DecoratedWindowStyle.light()
                        } else {
                            DecoratedWindowStyle.dark()
                        },
                    ),
            swingCompatMode = true,
        ) {
            DecoratedWindow(
                onCloseRequest = { exitApplication() },
                title = "Desktop Ai",
                icon = painterResource(Res.drawable.desktop_computer),
                onKeyEvent = { keyEvent ->
                    //  processKeyShortcuts(keyEvent = keyEvent, onNavigateTo = MainViewModel::onNavigateTo)
                    false
                },
                content = {
                    //TitleBarView()

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = JewelTheme.globalColors.panelBackground
                    ) {
                        ChatScreen()
                    }
                },
            )
        }


    }

