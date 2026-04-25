package com.volokhinaleksey.kash.domain.usecase

import com.volokhinaleksey.kash.domain.model.Category
import com.volokhinaleksey.kash.domain.model.Transaction
import com.volokhinaleksey.kash.domain.repository.CategoryRepository
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTransactionsUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) {
    operator fun invoke(): Flow<List<Pair<Transaction, Category>>> {
        return combine(
            transactionRepository.getAllTransactions(),
            categoryRepository.getAllCategories(),
        ) { transactions, categories ->
            val categoryMap = categories.associateBy { it.id }
            transactions
                .sortedByDescending { it.date }
                .mapNotNull { transaction ->
                    val category = categoryMap[transaction.categoryId] ?: return@mapNotNull null
                    transaction to category
                }
        }
    }
}
