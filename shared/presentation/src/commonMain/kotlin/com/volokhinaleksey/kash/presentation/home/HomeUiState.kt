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
    ) : HomeUiState
}

data class TransactionUiModel(
    val id: Long,
    val name: String,
    val category: String,
    val amount: String,
    val isIncome: Boolean,
    val iconName: String,
    val date: String,
)
