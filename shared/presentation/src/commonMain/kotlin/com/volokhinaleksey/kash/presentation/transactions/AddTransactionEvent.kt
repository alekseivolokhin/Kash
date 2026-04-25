package com.volokhinaleksey.kash.presentation.transactions

import com.volokhinaleksey.kash.domain.model.TransactionType

sealed interface AddTransactionEvent {
    data class TypeChanged(val type: TransactionType) : AddTransactionEvent
    data class AmountChanged(val amount: String) : AddTransactionEvent
    data class CategorySelected(val categoryId: Long) : AddTransactionEvent
    data class NoteChanged(val note: String) : AddTransactionEvent
    data object DateClicked : AddTransactionEvent
    data object SaveClicked : AddTransactionEvent
}
