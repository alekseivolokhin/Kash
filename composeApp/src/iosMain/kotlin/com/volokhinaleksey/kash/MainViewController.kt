package com.volokhinaleksey.kash

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.volokhinaleksey.kash.navigation.RootComponent

fun mainViewController() = ComposeUIViewController {
    val lifecycle = LifecycleRegistry()
    val rootComponent = RootComponent(DefaultComponentContext(lifecycle = lifecycle))
    App(rootComponent)
}
