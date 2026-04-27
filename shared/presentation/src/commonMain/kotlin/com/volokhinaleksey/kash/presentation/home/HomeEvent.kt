package com.volokhinaleksey.kash.presentation.home

import com.volokhinaleksey.kash.domain.model.Period

sealed interface HomeEvent {
    data class PeriodSelected(val period: Period) : HomeEvent
    data object AddTransactionClicked : HomeEvent
    data object ViewAllTransactionsClicked : HomeEvent
    data object ImportStatementClicked : HomeEvent
    data class AccountSelected(val accountId: String) : HomeEvent
    data object AllAccountsClicked : HomeEvent
}
