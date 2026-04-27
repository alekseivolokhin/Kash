package com.volokhinaleksey.kash.navigation.stats

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.volokhinaleksey.kash.domain.model.MonthlyAmount
import com.volokhinaleksey.kash.domain.model.Period
import com.volokhinaleksey.kash.domain.model.StatsSummary
import com.volokhinaleksey.kash.domain.usecase.GetStatsUseCase
import com.volokhinaleksey.kash.presentation.stats.MonthlyBarUiModel
import com.volokhinaleksey.kash.presentation.stats.StatsEvent
import com.volokhinaleksey.kash.presentation.stats.StatsUiState
import com.volokhinaleksey.kash.presentation.stats.TopCategoryUiModel
import com.volokhinaleksey.kash.presentation.util.formatTenge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
class StatsComponent(
    componentContext: ComponentContext,
    private val onAddTransaction: () -> Unit = {},
) : ComponentContext by componentContext, KoinComponent {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val getStats: GetStatsUseCase by inject()

    private val _selectedPeriod = MutableStateFlow(Period.THIS_MONTH)
    private val _uiState = MutableStateFlow<StatsUiState>(StatsUiState.Loading)
    val uiState: StateFlow<StatsUiState> = _uiState

    init {
        lifecycle.doOnDestroy { scope.cancel() }

        _selectedPeriod
            .flatMapLatest { period -> getStats(period).map { period to it } }
            .onEach { (period, summary) -> _uiState.value = summary.toUiState(period) }
            .flowOn(Dispatchers.Default)
            .launchIn(scope)
    }

    fun onEvent(event: StatsEvent) {
        when (event) {
            is StatsEvent.PeriodSelected -> _selectedPeriod.value = event.period
            is StatsEvent.AddTransactionClicked -> onAddTransaction()
        }
    }

    private fun StatsSummary.toUiState(period: Period): StatsUiState {
        if (!hasAnyTransactions) return StatsUiState.Empty

        val percent = computePercentChange(expenses, previousExpenses)
        return StatsUiState.Success(
            selectedPeriod = period,
            income = formatTenge(income),
            expenses = formatTenge(expenses),
            comparisonPercent = kotlin.math.abs(percent),
            isSpendingDown = expenses <= previousExpenses,
            monthlyChart = monthlyExpenses.toMonthlyBars(),
            topCategories = topCategories.map { category ->
                TopCategoryUiModel(
                    id = category.categoryId,
                    name = category.name,
                    iconName = category.icon,
                    amount = formatTenge(category.amount),
                    ratio = category.ratio,
                )
            },
        )
    }

    private fun List<MonthlyAmount>.toMonthlyBars(): List<MonthlyBarUiModel> {
        val maxAmount = maxOfOrNull { it.amount }?.takeIf { it > 0.0 } ?: 1.0
        return map { month ->
            MonthlyBarUiModel(
                label = month.monthLabel,
                ratio = (month.amount / maxAmount).toFloat().coerceIn(0f, 1f),
                isSelected = month.isCurrent,
            )
        }
    }

    private fun computePercentChange(current: Double, previous: Double): Int {
        if (previous <= 0.0) return 0
        val change = (current - previous) / previous * 100.0
        return change.roundToInt()
    }
}

