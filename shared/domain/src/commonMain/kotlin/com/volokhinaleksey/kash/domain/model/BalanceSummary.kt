package com.volokhinaleksey.kash.domain.model

data class BalanceSummary(
    val totalBalance: Double,
    val income: Double,
    val expenses: Double,
    val percentChangeFromLastMonth: Double,
)
