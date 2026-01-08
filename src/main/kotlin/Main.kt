package com.eltonkola.desktop

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

fun main(): Unit = application {
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
