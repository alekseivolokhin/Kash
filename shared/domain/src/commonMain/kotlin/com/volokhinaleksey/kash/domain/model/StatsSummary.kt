package com.volokhinaleksey.kash.domain.model

data class StatsSummary(
    val income: Double,
    val expenses: Double,
    val previousExpenses: Double,
    val monthlyExpenses: List<MonthlyAmount>,
    val topCategories: List<CategoryAmount>,
    val hasAnyTransactions: Boolean,
)

data class MonthlyAmount(
    val monthLabel: String,
    val amount: Double,
    val isCurrent: Boolean,
)

data class CategoryAmount(
    val categoryId: Long,
    val name: String,
    val icon: String,
    val amount: Double,
    val ratio: Float,
)
