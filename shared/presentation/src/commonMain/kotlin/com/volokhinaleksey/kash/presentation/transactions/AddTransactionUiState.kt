package com.volokhinaleksey.kash.presentation.transactions

import com.volokhinaleksey.kash.domain.model.TransactionType

enum class AddTxTab { Expense, Income, Transfer }

data class AddTransactionUiState(
    val type: TransactionType,
    val amount: String,
    val categories: List<CategoryUiModel>,
    val selectedCategoryId: Long?,
    val date: Long,
    val note: String,
    val tab: AddTxTab = AddTxTab.Expense,
    val accountName: String = "Kaspi Gold",
    val accountCurrency: String = "KZT",
    val accountBankId: String = "kaspi",
    val ratesStale: Boolean = false,
    val staleInfo: String? = null,
)

data class CategoryUiModel(
    val id: Long,
    val name: String,
    val iconName: String,
)
