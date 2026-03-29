package com.volokhinaleksey.kash.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.volokhinaleksey.kash.data.KashDatabaseQueries
import com.volokhinaleksey.kash.data.TransactionEntity
import com.volokhinaleksey.kash.domain.model.Transaction
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import com.volokhinaleksey.kash.mappers.asDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val queries: KashDatabaseQueries,
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> =
        queries.getAllTransactions()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map(TransactionEntity::asDomain) }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return queries.getTransactionById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.asDomain() }
            .firstOrNull()
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        queries.insertTransaction(
            amount = transaction.amount,
            type = transaction.type.name,
            category_id = transaction.categoryId,
            date = transaction.date,
            comment = transaction.comment
        )
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        queries.updateTransaction(
            id = transaction.id,
            amount = transaction.amount,
            type = transaction.type.name,
            category_id = transaction.categoryId,
            date = transaction.date,
            comment = transaction.comment
        )
    }

    override suspend fun deleteTransaction(id: Long) {
        queries.deleteTransaction(id)
    }
}
