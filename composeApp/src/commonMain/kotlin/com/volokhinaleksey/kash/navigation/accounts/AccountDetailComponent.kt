package com.volokhinaleksey.kash.navigation.accounts

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.presentation.accounts.Account
import com.volokhinaleksey.kash.presentation.accounts.AccountDetailEvent
import com.volokhinaleksey.kash.presentation.accounts.AccountDetailFilter
import com.volokhinaleksey.kash.presentation.accounts.AccountDetailTransaction
import com.volokhinaleksey.kash.presentation.accounts.AccountDetailUiState
import com.volokhinaleksey.kash.presentation.accounts.AccountType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AccountDetailComponent(
    componentContext: ComponentContext,
    accountId: String,
    private val onBack: () -> Unit,
    private val onMakeTransfer: () -> Unit,
) : ComponentContext by componentContext {

    private val account: Account = MOCK_ACCOUNTS.firstOrNull { it.id == accountId } ?: MOCK_ACCOUNTS.first()

    private val _uiState = MutableStateFlow(
        AccountDetailUiState(
            account = account,
            filter = AccountDetailFilter.All,
            transactions = mockTransactionsFor(account),
        )
    )
    val uiState: StateFlow<AccountDetailUiState> = _uiState

    fun onEvent(event: AccountDetailEvent) {
        when (event) {
            AccountDetailEvent.BackClicked -> onBack()
            AccountDetailEvent.MoreClicked -> Unit
            AccountDetailEvent.MakeTransferClicked -> onMakeTransfer()
            is AccountDetailEvent.FilterSelected ->
                _uiState.update { it.copy(filter = event.filter) }
        }
    }

    fun onBack() = onBack.invoke()
}

private fun mockTransactionsFor(account: Account): List<AccountDetailTransaction> {
    if (account.bank == Bank.Cash) return emptyList()
    if (account.type == AccountType.Deposit) return emptyList()
    return listOf(
        AccountDetailTransaction(
            id = "1",
            name = "Apple Store",
            category = "Electronics",
            time = "Today · 16:20",
            amount = -59900,
            iconName = "computer",
        ),
        AccountDetailTransaction(
            id = "2",
            name = "NoMad Kitchen",
            category = "Food",
            time = "Today · 12:30",
            amount = -12400,
            iconName = "restaurant",
        ),
        AccountDetailTransaction(
            id = "3",
            name = "Salary IT LLC",
            category = "Income",
            time = "Yesterday",
            amount = 145000,
            iconName = "work",
        ),
        AccountDetailTransaction(
            id = "4",
            name = "Yandex Taxi",
            category = "Transport",
            time = "Oct 10",
            amount = -8200,
            iconName = "directions_car",
        ),
    )
}
