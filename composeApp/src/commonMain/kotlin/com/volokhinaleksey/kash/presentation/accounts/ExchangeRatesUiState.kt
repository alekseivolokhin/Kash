package com.volokhinaleksey.kash.presentation.accounts

import androidx.compose.runtime.Immutable

@Immutable
data class ExchangeRatesUiState(
    val lastSyncLabel: String,
    val rates: List<ExchangeRate>,
)
