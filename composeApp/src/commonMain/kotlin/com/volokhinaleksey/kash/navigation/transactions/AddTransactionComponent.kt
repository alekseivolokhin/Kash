package com.volokhinaleksey.kash.navigation.transactions

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.domain.model.TransactionType
import com.volokhinaleksey.kash.presentation.transactions.AddTransactionEvent
import com.volokhinaleksey.kash.presentation.transactions.AddTransactionUiState
import com.volokhinaleksey.kash.presentation.transactions.CategoryUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock

class AddTransactionComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(
        AddTransactionUiState(
            type = TransactionType.EXPENSE,
            amount = "",
            categories = defaultCategories,
            selectedCategoryId = defaultCategories.first().id,
            date = Clock.System.now().toEpochMilliseconds(),
            note = "",
        )
    )
    val uiState: StateFlow<AddTransactionUiState> = _uiState

    fun onEvent(event: AddTransactionEvent) {
        when (event) {
            is AddTransactionEvent.TypeChanged ->
                _uiState.update { it.copy(type = event.type) }

            is AddTransactionEvent.AmountChanged ->
                _uiState.update { it.copy(amount = event.amount) }

            is AddTransactionEvent.CategorySelected ->
                _uiState.update { it.copy(selectedCategoryId = event.categoryId) }

            is AddTransactionEvent.NoteChanged ->
                _uiState.update { it.copy(note = event.note) }

            AddTransactionEvent.DateClicked -> Unit

            is AddTransactionEvent.DateChanged ->
                _uiState.update { it.copy(date = event.epochMillis) }

            AddTransactionEvent.SaveClicked -> onBack()
        }
    }

    fun onBackClicked() {
        onBack()
    }
}

private val defaultCategories = listOf(
    CategoryUiModel(id = 1, name = "Food", iconName = "restaurant"),
    CategoryUiModel(id = 2, name = "Transport", iconName = "directions_car"),
    CategoryUiModel(id = 3, name = "Entertainment", iconName = "theater_comedy"),
    CategoryUiModel(id = 4, name = "Salary", iconName = "work"),
    CategoryUiModel(id = 5, name = "Shopping", iconName = "shopping_bag"),
    CategoryUiModel(id = 6, name = "Electronics", iconName = "computer"),
)
