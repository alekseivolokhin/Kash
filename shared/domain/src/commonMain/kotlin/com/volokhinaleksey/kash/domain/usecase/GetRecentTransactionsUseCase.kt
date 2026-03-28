package com.volokhinaleksey.kash.domain.usecase

import com.volokhinaleksey.kash.domain.model.Category
import com.volokhinaleksey.kash.domain.model.Period
import com.volokhinaleksey.kash.domain.model.Transaction
import com.volokhinaleksey.kash.domain.repository.CategoryRepository
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class GetRecentTransactionsUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) {
    operator fun invoke(period: Period, limit: Int = 10): Flow<List<Pair<Transaction, Category>>> {
        return combine(
            transactionRepository.getAllTransactions(),
            categoryRepository.getAllCategories(),
        ) { transactions, categories ->
            val now = kotlin.time.Clock.System.now()
            val tz = TimeZone.currentSystemDefault()
            val currentDate = now.toLocalDateTime(tz).date
            val (startMillis, endMillis) = periodToRange(period, currentDate, tz)

            val categoryMap = categories.associateBy { it.id }

            transactions
                .filter { it.date in startMillis until endMillis }
                .sortedByDescending { it.date }
                .take(limit)
                .mapNotNull { transaction ->
                    val category = categoryMap[transaction.categoryId] ?: return@mapNotNull null
                    transaction to category
                }
        }
    }
}
