package com.volokhinaleksey.kash.presentation.accounts

import androidx.compose.runtime.Immutable

@Immutable
data class TransferUiState(
    val from: Account,
    val to: Account,
    val sendAmount: String,
    val sendCurrencySymbol: String,
    val receiveAmount: String,
    val receiveCurrencySymbol: String,
    val ratePair: String,
    val rateValue: String,
    val rateAuto: Boolean,
    val comment: String,
)
