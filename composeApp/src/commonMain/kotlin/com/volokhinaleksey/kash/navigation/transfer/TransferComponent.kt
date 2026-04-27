package com.volokhinaleksey.kash.navigation.transfer

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.presentation.accounts.Account
import com.volokhinaleksey.kash.presentation.accounts.AccountType
import com.volokhinaleksey.kash.presentation.accounts.TransferEvent
import com.volokhinaleksey.kash.presentation.accounts.TransferUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TransferComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val from = Account(
        id = "kaspi-gold",
        name = "Kaspi Gold",
        type = AccountType.Debit,
        currency = "KZT",
        balance = 425000,
        bank = Bank.Kaspi,
    )
    private val to = Account(
        id = "halyk-onay",
        name = "Halyk Onay",
        type = AccountType.Debit,
        currency = "USD",
        balance = 850,
        bank = Bank.Halyk,
    )

    private val _uiState = MutableStateFlow(
        TransferUiState(
            from = from,
            to = to,
            sendAmount = "50 000",
            sendCurrencySymbol = "₸",
            receiveAmount = "104.50",
            receiveCurrencySymbol = "$",
            ratePair = "USD / KZT",
            rateValue = "478.50",
            rateAuto = true,
            comment = "Top-up for travel",
        )
    )
    val uiState: StateFlow<TransferUiState> = _uiState

    fun onEvent(event: TransferEvent) {
        when (event) {
            TransferEvent.BackClicked -> onBack()
            TransferEvent.FromClicked -> Unit
            TransferEvent.ToClicked -> Unit
            TransferEvent.RateOverrideClicked -> Unit
            TransferEvent.SaveClicked -> onBack()
            is TransferEvent.SendAmountChanged ->
                _uiState.update { it.copy(sendAmount = event.value) }
            is TransferEvent.CommentChanged ->
                _uiState.update { it.copy(comment = event.value) }
        }
    }

    fun onBack() = onBack.invoke()
}
