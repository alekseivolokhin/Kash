package com.volokhinaleksey.kash.presentation.transactions

sealed interface TransactionsUiState {
    val searchQuery: String
    val filter: TransactionsFilter
    val availableCategoryFilters: List<String>

    data class Loading(
        override val searchQuery: String = "",
        override val filter: TransactionsFilter = TransactionsFilter.All,
        override val availableCategoryFilters: List<String> = emptyList(),
    ) : TransactionsUiState

    data class Content(
        override val searchQuery: String,
        override val filter: TransactionsFilter,
        override val availableCategoryFilters: List<String>,
        val groups: List<TransactionGroupUiModel>,
    ) : TransactionsUiState {
        val isEmpty: Boolean get() = groups.isEmpty()
    }
}

sealed interface TransactionsFilter {
    data object All : TransactionsFilter
    data object Income : TransactionsFilter
    data object Expense : TransactionsFilter
    data object Transfer : TransactionsFilter
    data object Account : TransactionsFilter
    data class Category(val name: String) : TransactionsFilter
}

data class TransactionGroupUiModel(
    val key: String,
    val label: TransactionGroupLabel,
    val items: List<TransactionRowUiModel>,
)

sealed interface TransactionGroupLabel {
    data object Today : TransactionGroupLabel
    data object Yesterday : TransactionGroupLabel
    data class Date(val text: String) : TransactionGroupLabel
}

data class TransactionRowUiModel(
    val id: Long,
    val name: String,
    val category: String,
    val time: String,
    val amount: String,
    val isIncome: Boolean,
    val iconName: String,
    val bankId: String? = null,
    val isTransfer: Boolean = false,
    val transferToBankId: String? = null,
    val transferConvertedAmount: String? = null,
)
