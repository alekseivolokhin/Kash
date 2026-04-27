package com.volokhinaleksey.kash.navigation.accounts

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.presentation.accounts.ExchangeRate
import com.volokhinaleksey.kash.presentation.accounts.ExchangeRatesEvent
import com.volokhinaleksey.kash.presentation.accounts.ExchangeRatesUiState
import com.volokhinaleksey.kash.presentation.accounts.RateSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExchangeRatesComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(
        ExchangeRatesUiState(
            lastSyncLabel = "today, 09:00",
            rates = listOf(
                ExchangeRate("USD / KZT", "478.50", RateSource.Auto, "Today, 09:00", false),
                ExchangeRate("EUR / KZT", "520.20", RateSource.Auto, "Today, 09:00", false),
                ExchangeRate("RUB / KZT", "5.18", RateSource.Manual, "Yesterday", false),
                ExchangeRate("GBP / KZT", "605.00", RateSource.Auto, "3 days ago", true),
            ),
        )
    )
    val uiState: StateFlow<ExchangeRatesUiState> = _uiState

    fun onEvent(event: ExchangeRatesEvent) {
        when (event) {
            ExchangeRatesEvent.BackClicked -> onBack()
            ExchangeRatesEvent.SyncClicked -> Unit
            is ExchangeRatesEvent.RateClicked -> Unit
        }
    }

    fun onBack() = onBack.invoke()
}
