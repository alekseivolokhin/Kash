package com.volokhinaleksey.kash.presentation.accounts

import androidx.compose.runtime.Immutable

@Immutable
data class AccountsUiState(
    val totalBalance: String,
    val totalCurrencyChips: List<CurrencyChip>,
    val groups: List<AccountGroup>,
) {
    val isEmpty: Boolean get() = groups.isEmpty()
}

@Immutable
data class CurrencyChip(
    val code: String,
    val amount: String,
)
