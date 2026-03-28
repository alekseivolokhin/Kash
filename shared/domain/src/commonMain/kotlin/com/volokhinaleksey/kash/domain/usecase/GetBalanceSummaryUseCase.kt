package com.volokhinaleksey.kash.domain.usecase

import com.volokhinaleksey.kash.domain.model.BalanceSummary
import com.volokhinaleksey.kash.domain.model.Period
import com.volokhinaleksey.kash.domain.model.TransactionType
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class GetBalanceSummaryUseCase(
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(period: Period): Flow<BalanceSummary> {
        return transactionRepository.getAllTransactions().map { allTransactions ->
            val now = kotlin.time.Clock.System.now()
            val tz = TimeZone.currentSystemDefault()
            val currentDate = now.toLocalDateTime(tz).date

            val (startMillis, endMillis) = periodToRange(period, currentDate, tz)
            val periodTransactions = allTransactions.filter { it.date in startMillis until endMillis }

            val income = periodTransactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }
            val expenses = periodTransactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }
            val totalBalance = income - expenses

            val previousPeriodRange = previousPeriodRange(period, currentDate, tz)
            val previousTransactions = allTransactions.filter { it.date in previousPeriodRange.first until previousPeriodRange.second }
            val previousIncome = previousTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val previousExpenses = previousTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val previousBalance = previousIncome - previousExpenses

            val percentChange = if (previousBalance != 0.0) {
                ((totalBalance - previousBalance) / kotlin.math.abs(previousBalance)) * 100
            } else {
                0.0
            }

            BalanceSummary(
                totalBalance = totalBalance,
                income = income,
                expenses = expenses,
                percentChangeFromLastMonth = percentChange,
            )
        }
    }
}

internal fun periodToRange(
    period: Period,
    currentDate: LocalDate,
    tz: TimeZone,
): Pair<Long, Long> {
    return when (period) {
        Period.THIS_MONTH -> {
            val start = LocalDate(currentDate.year, currentDate.month, 1)
            val nextMonth = start.plus(1, DateTimeUnit.MONTH)
            start.atStartOfDayIn(tz).toEpochMilliseconds() to nextMonth.atStartOfDayIn(tz).toEpochMilliseconds()
        }
        Period.LAST_MONTH -> {
            val start = LocalDate(currentDate.year, currentDate.month, 1).minus(1, DateTimeUnit.MONTH)
            val end = start.plus(1, DateTimeUnit.MONTH)
            start.atStartOfDayIn(tz).toEpochMilliseconds() to end.atStartOfDayIn(tz).toEpochMilliseconds()
        }
        Period.QUARTER -> {
            val quarterStartMonth = ((currentDate.monthNumber - 1) / 3) * 3 + 1
            val start = LocalDate(currentDate.year, quarterStartMonth, 1)
            val end = start.plus(3, DateTimeUnit.MONTH)
            start.atStartOfDayIn(tz).toEpochMilliseconds() to end.atStartOfDayIn(tz).toEpochMilliseconds()
        }
    }
}

internal fun previousPeriodRange(
    period: Period,
    currentDate: LocalDate,
    tz: TimeZone,
): Pair<Long, Long> {
    return when (period) {
        Period.THIS_MONTH -> periodToRange(Period.LAST_MONTH, currentDate, tz)
        Period.LAST_MONTH -> {
            val start = LocalDate(currentDate.year, currentDate.month, 1).minus(2, DateTimeUnit.MONTH)
            val end = start.plus(1, DateTimeUnit.MONTH)
            start.atStartOfDayIn(tz).toEpochMilliseconds() to end.atStartOfDayIn(tz).toEpochMilliseconds()
        }
        Period.QUARTER -> {
            val quarterStartMonth = ((currentDate.monthNumber - 1) / 3) * 3 + 1
            val start = LocalDate(currentDate.year, quarterStartMonth, 1).minus(3, DateTimeUnit.MONTH)
            val end = start.plus(3, DateTimeUnit.MONTH)
            start.atStartOfDayIn(tz).toEpochMilliseconds() to end.atStartOfDayIn(tz).toEpochMilliseconds()
        }
    }
}
