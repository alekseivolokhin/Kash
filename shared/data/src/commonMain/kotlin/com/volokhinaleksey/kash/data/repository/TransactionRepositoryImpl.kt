package com.volokhinaleksey.kash.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.volokhinaleksey.kash.data.KashDatabaseQueries
import com.volokhinaleksey.kash.domain.model.Transaction
import com.volokhinaleksey.kash.domain.model.TransactionType
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val queries: KashDatabaseQueries,
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        queries.getAllTransactions()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { entity ->
                    Transaction(
                        id = entity.id,
                        amount = entity.amount,
                        type = TransactionType.valueOf(entity.type),
                        categoryId = entity.category_id,
                        date = entity.date,
                        comment = entity.comment,
                    )
                }
            }

    override suspend fun getTransactionById(id: Long): Transaction? {
        TODO("By analogy with getAllTransactions")
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        TODO("By analogy with getAllTransactions")
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        TODO("By analogy with getAllTransactions")
    }

    override suspend fun deleteTransaction(id: Long) {
        TODO("By analogy with getAllTransactions")
    }
}
