package com.volokhinaleksey.kash.domain.usecase

import com.volokhinaleksey.kash.domain.model.CategoryAmount
import com.volokhinaleksey.kash.domain.model.MonthlyAmount
import com.volokhinaleksey.kash.domain.model.Period
import com.volokhinaleksey.kash.domain.model.StatsSummary
import com.volokhinaleksey.kash.domain.model.TransactionType
import com.volokhinaleksey.kash.domain.repository.CategoryRepository
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class GetStatsUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) {
    operator fun invoke(period: Period): Flow<StatsSummary> {
        return combine(
            transactionRepository.getAllTransactions(),
            categoryRepository.getAllCategories(),
        ) { transactions, categories ->
            val tz = TimeZone.currentSystemDefault()
            val currentDate = kotlin.time.Clock.System.now().toLocalDateTime(tz).date

            val (start, end) = periodToRange(period, currentDate, tz)
            val periodTransactions = transactions.filter { it.date in start until end }

            val income = periodTransactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }
            val expenses = periodTransactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            val (prevStart, prevEnd) = previousPeriodRange(period, currentDate, tz)
            val previousExpenses = transactions
                .filter { it.date in prevStart until prevEnd && it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            val categoryMap = categories.associateBy { it.id }
            val topCategories = periodTransactions
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { it.categoryId }
                .mapNotNull { (categoryId, txs) ->
                    val category = categoryMap[categoryId] ?: return@mapNotNull null
                    val total = txs.sumOf { it.amount }
                    category to total
                }
                .sortedByDescending { it.second }
                .take(TOP_CATEGORIES_LIMIT)
                .let { topPairs ->
                    val maxAmount = topPairs.maxOfOrNull { it.second } ?: 0.0
                    topPairs.map { (category, amount) ->
                        CategoryAmount(
                            categoryId = category.id,
                            name = category.name,
                            icon = category.icon,
                            amount = amount,
                            ratio = if (maxAmount > 0.0) (amount / maxAmount).toFloat() else 0f,
                        )
                    }
                }

            val monthlyExpenses = lastSixMonthlyExpenses(transactions, currentDate, tz)

            StatsSummary(
                income = income,
                expenses = expenses,
                previousExpenses = previousExpenses,
                monthlyExpenses = monthlyExpenses,
                topCategories = topCategories,
                hasAnyTransactions = transactions.isNotEmpty(),
            )
        }
    }

    private fun lastSixMonthlyExpenses(
        transactions: List<com.volokhinaleksey.kash.domain.model.Transaction>,
        currentDate: LocalDate,
        tz: TimeZone,
    ): List<MonthlyAmount> {
        val months = (5 downTo 0).map { offset ->
            val anchor = LocalDate(currentDate.year, currentDate.month, 1).minus(offset, DateTimeUnit.MONTH)
            val nextAnchor = anchor.plus(1, DateTimeUnit.MONTH)
            val rangeStart = anchor.atStartOfDayIn(tz).toEpochMilliseconds()
            val rangeEnd = nextAnchor.atStartOfDayIn(tz).toEpochMilliseconds()
            Triple(anchor, rangeStart, rangeEnd)
        }
        return months.map { (anchor, start, end) ->
            val total = transactions
                .filter { it.date in start until end && it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }
            MonthlyAmount(
                monthLabel = MONTH_LABELS[anchor.monthNumber - 1],
                amount = total,
                isCurrent = anchor.year == currentDate.year && anchor.monthNumber == currentDate.monthNumber,
            )
        }
    }

    private companion object {
        const val TOP_CATEGORIES_LIMIT = 4
        val MONTH_LABELS = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
        )
    }
}
