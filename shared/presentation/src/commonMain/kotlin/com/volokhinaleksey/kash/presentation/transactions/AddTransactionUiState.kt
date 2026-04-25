package com.volokhinaleksey.kash.presentation.transactions

import com.volokhinaleksey.kash.domain.model.TransactionType

data class AddTransactionUiState(
    val type: TransactionType,
    val amount: String,
    val categories: List<CategoryUiModel>,
    val selectedCategoryId: Long?,
    val date: Long,
    val note: String,
)

data class CategoryUiModel(
    val id: Long,
    val name: String,
    val iconName: String,
)
