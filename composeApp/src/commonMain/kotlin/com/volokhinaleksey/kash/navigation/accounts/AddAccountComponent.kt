package com.volokhinaleksey.kash.navigation.accounts

import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.presentation.accounts.AccountType
import com.volokhinaleksey.kash.presentation.accounts.AddAccountEvent
import com.volokhinaleksey.kash.presentation.accounts.AddAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AddAccountComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val onPickInstitution: () -> Unit,
    private val onPickCurrency: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(
        AddAccountUiState(
            type = AccountType.Debit,
            initialBalance = "425 000",
            name = "Kaspi Gold",
            bank = Bank.Kaspi,
            currencyCode = "KZT",
            currencyDisplay = "KZT · Kazakhstani Tenge",
            colorPalette = ADD_ACCOUNT_COLORS,
            selectedColorIndex = 0,
        )
    )
    val uiState: StateFlow<AddAccountUiState> = _uiState

    fun onEvent(event: AddAccountEvent) {
        when (event) {
            AddAccountEvent.BackClicked -> onBack()
            AddAccountEvent.InstitutionClicked -> onPickInstitution()
            AddAccountEvent.CurrencyClicked -> onPickCurrency()
            AddAccountEvent.CreateClicked -> onBack()
            is AddAccountEvent.TypeSelected ->
                _uiState.update { it.copy(type = event.type) }
            is AddAccountEvent.BalanceChanged ->
                _uiState.update { it.copy(initialBalance = event.balance) }
            is AddAccountEvent.NameChanged ->
                _uiState.update { it.copy(name = event.name) }
            is AddAccountEvent.ColorSelected ->
                _uiState.update { it.copy(selectedColorIndex = event.index) }
        }
    }

    fun onBack() = onBack.invoke()
}

private val ADD_ACCOUNT_COLORS: List<Color> = listOf(
    Color(0xFF1F3D2C),
    Color(0xFF7A4A1E),
    Color(0xFF2C4A66),
    Color(0xFF5C3A66),
    Color(0xFF7A3A30),
    Color(0xFF5A4E2A),
)
