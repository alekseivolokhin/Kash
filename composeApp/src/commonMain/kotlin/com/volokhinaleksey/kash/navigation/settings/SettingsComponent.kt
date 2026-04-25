package com.volokhinaleksey.kash.navigation.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.volokhinaleksey.kash.platform.getAppVersion
import com.volokhinaleksey.kash.presentation.settings.SettingsEvent
import com.volokhinaleksey.kash.presentation.settings.SettingsUiState
import com.volokhinaleksey.kash.presentation.settings.ThemeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SettingsComponent(
    componentContext: ComponentContext,
    themeMode: StateFlow<ThemeMode>,
    private val onThemeModeChange: (ThemeMode) -> Unit,
    private val onImportClicked: () -> Unit = {},
    private val onExportClicked: () -> Unit = {},
) : ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    init {
        lifecycle.doOnDestroy { scope.cancel() }
    }

    private val baseState = SettingsUiState(
        userName = "Alex Mercer",
        userInitials = "AM",
        isPremium = true,
        baseCurrencyCode = "KZT",
        baseCurrencySymbol = "₸",
        themeMode = themeMode.value,
        appVersion = getAppVersion(),
    )

    val uiState: StateFlow<SettingsUiState> = themeMode
        .map { mode -> baseState.copy(themeMode = mode) }
        .stateIn(scope, SharingStarted.Eagerly, baseState)

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ThemeChanged -> onThemeModeChange(event.mode)
            SettingsEvent.ImportTransactionsClicked -> onImportClicked()
            SettingsEvent.ExportCsvClicked -> onExportClicked()
            SettingsEvent.BaseCurrencyClicked,
            SettingsEvent.SignOutClicked -> Unit
        }
    }
}
