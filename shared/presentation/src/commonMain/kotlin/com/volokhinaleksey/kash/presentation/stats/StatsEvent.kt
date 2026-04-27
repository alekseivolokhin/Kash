package com.volokhinaleksey.kash.presentation.stats

import com.volokhinaleksey.kash.domain.model.Period

sealed interface StatsEvent {
    data class PeriodSelected(val period: Period) : StatsEvent
    data object AddTransactionClicked : StatsEvent
}
