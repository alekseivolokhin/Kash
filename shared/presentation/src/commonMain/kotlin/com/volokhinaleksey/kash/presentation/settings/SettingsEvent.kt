package com.volokhinaleksey.kash.presentation.settings

sealed interface SettingsEvent {
    data object BaseCurrencyClicked : SettingsEvent
    data class ThemeChanged(val mode: ThemeMode) : SettingsEvent
    data object ImportTransactionsClicked : SettingsEvent
    data object ExportCsvClicked : SettingsEvent
    data object SignOutClicked : SettingsEvent
    data object AccountsClicked : SettingsEvent
    data object ExchangeRatesClicked : SettingsEvent
}
