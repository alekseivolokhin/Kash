package com.volokhinaleksey.kash.data.repository

import com.volokhinaleksey.kash.domain.model.Transaction
import com.volokhinaleksey.kash.domain.model.TransactionType
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class MockTransactionRepository : TransactionRepository {

    private val now = Clock.System.now()
    private val tz = TimeZone.currentSystemDefault()
    private val today = now.toLocalDateTime(tz).date

    private fun daysAgo(days: Int): Long {
        val date = today.minus(days, DateTimeUnit.DAY)
        return date.atStartOfDayIn(tz).toEpochMilliseconds()
    }

    private val mockTransactions = listOf(
        // This month — expenses
        Transaction(id = 1, amount = 59_900.0, type = TransactionType.EXPENSE, categoryId = 1, date = daysAgo(0), comment = "Apple Store"),
        Transaction(id = 2, amount = 12_400.0, type = TransactionType.EXPENSE, categoryId = 3, date = daysAgo(1), comment = "NoMad Kitchen"),
        Transaction(id = 3, amount = 8_500.0, type = TransactionType.EXPENSE, categoryId = 4, date = daysAgo(2), comment = "Gas Station"),
        Transaction(id = 4, amount = 15_200.0, type = TransactionType.EXPENSE, categoryId = 5, date = daysAgo(3), comment = "Netflix & Spotify"),
        Transaction(id = 5, amount = 24_500.0, type = TransactionType.EXPENSE, categoryId = 3, date = daysAgo(5), comment = "Grocery Store"),

        // This month — income
        Transaction(id = 6, amount = 145_000.0, type = TransactionType.INCOME, categoryId = 2, date = daysAgo(1), comment = "Freelance Project"),
        Transaction(id = 7, amount = 305_000.0, type = TransactionType.INCOME, categoryId = 2, date = daysAgo(10), comment = "Salary"),

        // Last month — for comparison
        Transaction(id = 8, amount = 280_000.0, type = TransactionType.INCOME, categoryId = 2, date = daysAgo(35), comment = "Salary"),
        Transaction(id = 9, amount = 120_000.0, type = TransactionType.INCOME, categoryId = 2, date = daysAgo(40), comment = "Freelance Project"),
        Transaction(id = 10, amount = 45_000.0, type = TransactionType.EXPENSE, categoryId = 1, date = daysAgo(33), comment = "New Headphones"),
        Transaction(id = 11, amount = 32_000.0, type = TransactionType.EXPENSE, categoryId = 3, date = daysAgo(36), comment = "Restaurant"),
        Transaction(id = 12, amount = 18_000.0, type = TransactionType.EXPENSE, categoryId = 4, date = daysAgo(38), comment = "Gas Station"),
    )

    private val transactions = MutableStateFlow(mockTransactions)

    override fun getAllTransactions(): Flow<List<Transaction>> = transactions

    override suspend fun getTransactionById(id: Long): Transaction? =
        transactions.value.find { it.id == id }

    override suspend fun insertTransaction(transaction: Transaction) {
        transactions.value = transactions.value + transaction
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactions.value = transactions.value.map {
            if (it.id == transaction.id) transaction else it
        }
    }

    override suspend fun deleteTransaction(id: Long) {
        transactions.value = transactions.value.filter { it.id != id }
    }
}
