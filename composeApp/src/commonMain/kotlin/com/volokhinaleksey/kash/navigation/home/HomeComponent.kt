package com.volokhinaleksey.kash.navigation.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.volokhinaleksey.kash.domain.model.Period
import com.volokhinaleksey.kash.domain.model.TransactionType
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import com.volokhinaleksey.kash.domain.usecase.GetBalanceSummaryUseCase
import com.volokhinaleksey.kash.domain.usecase.GetRecentTransactionsUseCase
import com.volokhinaleksey.kash.presentation.home.HomeAccountChip
import com.volokhinaleksey.kash.presentation.home.HomeCurrencyChip
import com.volokhinaleksey.kash.presentation.home.HomeEvent
import com.volokhinaleksey.kash.presentation.home.HomeUiState
import com.volokhinaleksey.kash.presentation.home.TransactionUiModel
import com.volokhinaleksey.kash.presentation.util.formatDateShort
import com.volokhinaleksey.kash.presentation.util.formatTenge
import com.volokhinaleksey.kash.presentation.util.formatTengeWithSign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.roundToInt

class HomeComponent(
    componentContext: ComponentContext,
    private val onNavigateToAddTransaction: () -> Unit,
    private val onNavigateToTransactions: () -> Unit,
    private val onNavigateToImport: () -> Unit,
    private val onNavigateToAccounts: () -> Unit = {},
    private val onNavigateToAccountDetail: (String) -> Unit = {},
) : ComponentContext by componentContext, KoinComponent {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val getBalanceSummary: GetBalanceSummaryUseCase by inject()
    private val getRecentTransactions: GetRecentTransactionsUseCase by inject()
    private val transactionRepository: TransactionRepository by inject()

    private val _selectedPeriod = MutableStateFlow(Period.THIS_MONTH)
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        lifecycle.doOnDestroy { scope.cancel() }

        _selectedPeriod.flatMapLatest { period ->
            combine(
                transactionRepository.getAllTransactions(),
                getBalanceSummary(period),
                getRecentTransactions(period),
            ) { allTransactions, summary, transactions ->
                if (allTransactions.isEmpty()) {
                    return@combine HomeUiState.Empty
                }

                val transactionModels = transactions.map { (transaction, category) ->
                    val isIncome = transaction.type == TransactionType.INCOME
                    TransactionUiModel(
                        id = transaction.id,
                        name = transaction.comment.ifEmpty { category.name },
                        category = category.name,
                        amount = formatTengeWithSign(transaction.amount, isIncome),
                        isIncome = isIncome,
                        iconName = category.icon,
                        date = formatDateShort(transaction.date),
                    )
                }

                val percentRounded = summary.percentChangeFromLastMonth.roundToInt()
                val percentSign = if (percentRounded >= 0) "+" else ""

                HomeUiState.Success(
                    totalBalance = formatTenge(summary.totalBalance),
                    percentChange = "${percentSign}${percentRounded}% vs last month",
                    isPositiveChange = summary.percentChangeFromLastMonth >= 0,
                    income = formatTenge(summary.income),
                    expenses = formatTenge(summary.expenses),
                    selectedPeriod = period,
                    recentTransactions = transactionModels,
                    currencyChips = MOCK_CURRENCY_CHIPS,
                    accountsStrip = MOCK_ACCOUNTS_STRIP,
                )
            }
        }.onEach { _uiState.value = it }
            .flowOn(Dispatchers.Default)
            .launchIn(scope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.PeriodSelected -> _selectedPeriod.value = event.period
            is HomeEvent.AddTransactionClicked -> onNavigateToAddTransaction()
            is HomeEvent.ViewAllTransactionsClicked -> onNavigateToTransactions()
            is HomeEvent.ImportStatementClicked -> onNavigateToImport()
            is HomeEvent.AccountSelected -> onNavigateToAccountDetail(event.accountId)
            HomeEvent.AllAccountsClicked -> onNavigateToAccounts()
        }
    }

}

private val MOCK_CURRENCY_CHIPS = listOf(
    HomeCurrencyChip("USD", "850"),
    HomeCurrencyChip("EUR", "120"),
)

private val MOCK_ACCOUNTS_STRIP = listOf(
    HomeAccountChip(
        id = "kaspi-gold",
        name = "Kaspi Gold",
        typeShort = "DEBIT",
        balance = "425 000",
        currencySymbol = "₸",
        bankId = "kaspi",
        selected = true,
    ),
    HomeAccountChip(
        id = "kaspi-red",
        name = "Kaspi Red",
        typeShort = "CREDIT",
        balance = "−42 000",
        currencySymbol = "₸",
        bankId = "kaspi",
    ),
    HomeAccountChip(
        id = "halyk-onay",
        name = "Halyk Onay",
        typeShort = "DEBIT",
        balance = "850",
        currencySymbol = "$",
        bankId = "halyk",
    ),
    HomeAccountChip(
        id = "wallet",
        name = "Wallet",
        typeShort = "CASH",
        balance = "25 000",
        currencySymbol = "₸",
        bankId = "cash",
    ),
)
