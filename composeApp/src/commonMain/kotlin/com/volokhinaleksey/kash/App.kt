package com.volokhinaleksey.kash

import androidx.compose.runtime.Composable
import com.volokhinaleksey.kash.navigation.RootComponent
import com.volokhinaleksey.kash.navigation.RootContent
import com.volokhinaleksey.kash.theme.KashTheme

@Composable
fun App(rootComponent: RootComponent) {
    KashTheme {
        RootContent(rootComponent)
    }
}
