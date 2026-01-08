package com.eltonkola.desktop

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltonkola.desktop.chat.ChatScreen
import com.eltonkola.desktop.desktopapp.generated.resources.Res
import com.eltonkola.desktop.desktopapp.generated.resources.logo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun App(
    exitApplication: () -> Unit,
    viewModel: AppViewModel = viewModel { AppViewModel() },
){
    
    val darkTheme by viewModel.darkTheme.collectAsState()
    val panelOpened by viewModel.panelOpened.collectAsState()
    
    IntUiTheme(
        theme = if (darkTheme) {
            JewelTheme.darkThemeDefinition()
        } else {
            JewelTheme.lightThemeDefinition()
        },
        styling =
            ComponentStyling.default()
                .decoratedWindow(
                    titleBarStyle = if (darkTheme) {
                        TitleBarStyle.dark()
                    } else {
                        TitleBarStyle.light()
                    },
                    windowStyle = if (darkTheme) {
                        DecoratedWindowStyle.dark()
                    } else {
                        DecoratedWindowStyle.light()
                    },
                ),
        swingCompatMode = true,
    ) {
        DecoratedWindow(
            onCloseRequest = { exitApplication() },
            title = "Desktop Ai",
            icon = painterResource(Res.drawable.logo),
            content = {
                TitleBarView(
                    darkTheme = darkTheme,
                    panelOpened = panelOpened,
                    switchTheme = {
                        viewModel.toggleTheme()
                    },
                    onSidebarClick = {
                        viewModel.togglePanel()
                    }
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = JewelTheme.globalColors.panelBackground
                ) {
                    ChatScreen(panelOpened)
                }
            },
        )
    }


}


class AppViewModel : ViewModel() {
    private val _darkTheme = MutableStateFlow(currentSystemTheme == SystemTheme.DARK)
    val darkTheme: StateFlow<Boolean> = _darkTheme.asStateFlow()

    private val _panelOpened = MutableStateFlow(true)
    val panelOpened: StateFlow<Boolean> = _panelOpened.asStateFlow()

    init {
        JewelLogger.getInstance("StandaloneSample").info("Starting Jewel Standalone sample")
    }

    fun toggleTheme() {
        _darkTheme.value = !_darkTheme.value
    }

    fun togglePanel() {
        _panelOpened.value = !_panelOpened.value
    }

}


