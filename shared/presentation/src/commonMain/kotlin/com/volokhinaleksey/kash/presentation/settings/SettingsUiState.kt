package com.volokhinaleksey.kash.presentation.settings

data class SettingsUiState(
    val userName: String,
    val userInitials: String,
    val isPremium: Boolean,
    val baseCurrencyCode: String,
    val baseCurrencySymbol: String,
    val themeMode: ThemeMode,
    val appVersion: String,
    val accountsCount: Int = 0,
    val sampleRate: String = "",
)
