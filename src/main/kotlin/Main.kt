package com.eltonkola.desktop

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import co.touchlab.kermit.Logger

fun main() {

    AppLogging.init()

    val logger = Logger.withTag("App")

    try {
        logger.i("Application starting")

        application {

            val viewModelStore = remember { ViewModelStore() }
            val viewModelStoreOwner = remember {
                object : ViewModelStoreOwner {
                    override val viewModelStore = viewModelStore
                }
            }
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                App(exitApplication = ::exitApplication)
            }

        }
    } catch (e: Throwable) {
        logger.e("Fatal error", e)
        throw e
    }
}

