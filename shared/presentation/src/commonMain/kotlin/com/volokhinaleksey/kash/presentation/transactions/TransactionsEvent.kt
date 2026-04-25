package com.volokhinaleksey.kash.presentation.transactions

sealed interface TransactionsEvent {
    data class SearchQueryChanged(val query: String) : TransactionsEvent
    data class FilterSelected(val filter: TransactionsFilter) : TransactionsEvent
    data object AddTransactionClicked : TransactionsEvent
}
