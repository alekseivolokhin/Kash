package com.volokhinaleksey.kash.navigation.accounts

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.presentation.accounts.InstitutionPickerEvent
import com.volokhinaleksey.kash.presentation.accounts.InstitutionPickerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class InstitutionPickerComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(
        InstitutionPickerUiState(
            searchQuery = "",
            selectedBank = Bank.Kaspi,
            popular = listOf(Bank.Kaspi, Bank.Halyk, Bank.Forte, Bank.Jusan),
            all = listOf(Bank.Atf, Bank.Bereke, Bank.Revolut, Bank.Wise),
        )
    )
    val uiState: StateFlow<InstitutionPickerUiState> = _uiState

    fun onEvent(event: InstitutionPickerEvent) {
        when (event) {
            InstitutionPickerEvent.BackClicked -> onBack()
            InstitutionPickerEvent.NoBankClicked -> {
                _uiState.update { it.copy(selectedBank = Bank.Cash) }
                onBack()
            }
            is InstitutionPickerEvent.SearchQueryChanged ->
                _uiState.update { it.copy(searchQuery = event.query) }
            is InstitutionPickerEvent.BankSelected -> {
                _uiState.update { it.copy(selectedBank = event.bank) }
                onBack()
            }
        }
    }

    fun onBack() = onBack.invoke()
}
