package com.volokhinaleksey.kash.presentation.home

import com.volokhinaleksey.kash.domain.model.Period

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Success(
        val totalBalance: String,
        val percentChange: String,
        val isPositiveChange: Boolean,
        val income: String,
        val expenses: String,
        val selectedPeriod: Period,
        val recentTransactions: List<TransactionUiModel>,
        val currencyChips: List<HomeCurrencyChip> = emptyList(),
        val accountsStrip: List<HomeAccountChip> = emptyList(),
    ) : HomeUiState
}

data class HomeCurrencyChip(
    val code: String,
    val amount: String,
)

data class HomeAccountChip(
    val id: String,
    val name: String,
    val typeShort: String,
    val balance: String,
    val currencySymbol: String,
    val bankId: String,
    val selected: Boolean = false,
)

data class TransactionUiModel(
    val id: Long,
    val name: String,
    val category: String,
    val amount: String,
    val isIncome: Boolean,
    val iconName: String,
    val date: String,
    val bankId: String = "cash",
)
