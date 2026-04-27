package com.volokhinaleksey.kash.presentation.stats

import com.volokhinaleksey.kash.domain.model.Period

sealed interface StatsUiState {
    data object Loading : StatsUiState
    data object Empty : StatsUiState
    data class Success(
        val selectedPeriod: Period,
        val income: String,
        val expenses: String,
        val comparisonPercent: Int,
        val isSpendingDown: Boolean,
        val monthlyChart: List<MonthlyBarUiModel>,
        val topCategories: List<TopCategoryUiModel>,
    ) : StatsUiState
}

data class MonthlyBarUiModel(
    val label: String,
    val ratio: Float,
    val isSelected: Boolean,
)

data class TopCategoryUiModel(
    val id: Long,
    val name: String,
    val iconName: String,
    val amount: String,
    val ratio: Float,
)
