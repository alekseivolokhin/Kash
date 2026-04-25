package com.volokhinaleksey.kash

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.volokhinaleksey.kash.navigation.RootComponent
import com.volokhinaleksey.kash.navigation.RootContent
import com.volokhinaleksey.kash.presentation.settings.ThemeMode
import com.volokhinaleksey.kash.theme.KashTheme

@Composable
fun App(rootComponent: RootComponent) {
    val themeMode by rootComponent.themeMode.collectAsState()
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    KashTheme(darkTheme = darkTheme) {
        RootContent(rootComponent)
    }
}
