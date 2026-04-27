package com.volokhinaleksey.kash.navigation.transactions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.volokhinaleksey.kash.domain.model.Category
import com.volokhinaleksey.kash.domain.model.Transaction
import com.volokhinaleksey.kash.domain.model.TransactionType
import com.volokhinaleksey.kash.domain.usecase.GetTransactionsUseCase
import com.volokhinaleksey.kash.presentation.transactions.TransactionGroupLabel
import com.volokhinaleksey.kash.presentation.transactions.TransactionGroupUiModel
import com.volokhinaleksey.kash.presentation.transactions.TransactionRowUiModel
import com.volokhinaleksey.kash.presentation.transactions.TransactionsEvent
import com.volokhinaleksey.kash.presentation.transactions.TransactionsFilter
import com.volokhinaleksey.kash.presentation.transactions.TransactionsUiState
import com.volokhinaleksey.kash.presentation.util.DayLabel
import com.volokhinaleksey.kash.presentation.util.dayLabelFor
import com.volokhinaleksey.kash.presentation.util.formatTengeWithSign
import com.volokhinaleksey.kash.presentation.util.formatTimeOfDay
import com.volokhinaleksey.kash.presentation.util.startOfDayMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TransactionsComponent(
    componentContext: ComponentContext,
    private val onNavigateToAddTransaction: () -> Unit,
) : ComponentContext by componentContext, KoinComponent {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val getTransactions: GetTransactionsUseCase by inject()

    private val _searchQuery = MutableStateFlow("")
    private val _filter = MutableStateFlow<TransactionsFilter>(TransactionsFilter.All)
    private val _uiState = MutableStateFlow<TransactionsUiState>(TransactionsUiState.Loading())
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()

    init {
        lifecycle.doOnDestroy { scope.cancel() }

        combine(
            getTransactions(),
            _searchQuery,
            _filter,
        ) { transactions, query, filter ->
            buildState(transactions, query, filter)
        }
            .onEach { _uiState.value = it }
            .flowOn(Dispatchers.Default)
            .launchIn(scope)
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.SearchQueryChanged -> _searchQuery.value = event.query
            is TransactionsEvent.FilterSelected -> _filter.value = event.filter
            TransactionsEvent.AddTransactionClicked -> onNavigateToAddTransaction()
        }
    }

    private fun buildState(
        transactions: List<Pair<Transaction, Category>>,
        query: String,
        filter: TransactionsFilter,
    ): TransactionsUiState {
        val availableCategories = transactions.map { it.second.name }.distinct()

        val trimmedQuery = query.trim()
        val filtered = transactions.filter { pair ->
            matchesFilter(pair.first, pair.second, filter) &&
                matchesQuery(pair.first, pair.second, trimmedQuery)
        }

        val groups = filtered
            .groupBy { startOfDayMillis(it.first.date) }
            .entries
            .sortedByDescending { it.key }
            .map { entry ->
                val dayMillis = entry.key
                TransactionGroupUiModel(
                    key = dayMillis.toString(),
                    label = dayLabelFor(dayMillis).toUi(),
                    items = entry.value.map { pair -> pair.first.toUi(pair.second) },
                )
            }

        return TransactionsUiState.Content(
            searchQuery = query,
            filter = filter,
            availableCategoryFilters = availableCategories,
            groups = groups,
        )
    }

    private fun matchesFilter(
        transaction: Transaction,
        category: Category,
        filter: TransactionsFilter,
    ): Boolean = when (filter) {
        TransactionsFilter.All -> true
        TransactionsFilter.Income -> transaction.type == TransactionType.INCOME
        TransactionsFilter.Expense -> transaction.type == TransactionType.EXPENSE
        TransactionsFilter.Transfer -> false
        TransactionsFilter.Account -> true
        is TransactionsFilter.Category -> category.name.equals(filter.name, ignoreCase = true)
    }

    private fun matchesQuery(
        transaction: Transaction,
        category: Category,
        query: String,
    ): Boolean {
        if (query.isEmpty()) return true
        return transaction.comment.contains(query, ignoreCase = true) ||
            category.name.contains(query, ignoreCase = true)
    }

    private fun Transaction.toUi(category: Category): TransactionRowUiModel {
        val isIncome = type == TransactionType.INCOME
        return TransactionRowUiModel(
            id = id,
            name = comment.ifEmpty { category.name },
            category = category.name,
            time = formatTimeOfDay(date),
            amount = formatTengeWithSign(amount, isIncome),
            isIncome = isIncome,
            iconName = category.icon,
        )
    }

    private fun DayLabel.toUi(): TransactionGroupLabel = when (this) {
        DayLabel.Today -> TransactionGroupLabel.Today
        DayLabel.Yesterday -> TransactionGroupLabel.Yesterday
        is DayLabel.Date -> TransactionGroupLabel.Date(text)
    }
}
