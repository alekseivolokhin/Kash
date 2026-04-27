package com.volokhinaleksey.kash.presentation.accounts

sealed interface ExchangeRatesEvent {
    data object BackClicked : ExchangeRatesEvent
    data object SyncClicked : ExchangeRatesEvent
    data class RateClicked(val pair: String) : ExchangeRatesEvent
}
