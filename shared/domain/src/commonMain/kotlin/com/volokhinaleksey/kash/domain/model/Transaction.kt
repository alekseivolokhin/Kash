package com.volokhinaleksey.kash.domain.model

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val date: Long,
    val comment: String = "",
)

enum class TransactionType {
    INCOME,
    EXPENSE,
}
