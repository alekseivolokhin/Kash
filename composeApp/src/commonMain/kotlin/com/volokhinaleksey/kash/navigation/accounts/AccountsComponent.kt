package com.volokhinaleksey.kash.navigation.accounts

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.presentation.accounts.Account
import com.volokhinaleksey.kash.presentation.accounts.AccountGroup
import com.volokhinaleksey.kash.presentation.accounts.AccountType
import com.volokhinaleksey.kash.presentation.accounts.AccountsEvent
import com.volokhinaleksey.kash.presentation.accounts.AccountsUiState
import com.volokhinaleksey.kash.presentation.accounts.CurrencyChip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AccountsComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val onAddAccount: () -> Unit,
    private val onAccountSelected: (String) -> Unit,
    private val onImportStatement: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(MOCK_STATE)
    val uiState: StateFlow<AccountsUiState> = _uiState

    fun onEvent(event: AccountsEvent) {
        when (event) {
            AccountsEvent.AddAccountClicked -> onAddAccount()
            AccountsEvent.ImportStatementClicked -> onImportStatement()
            is AccountsEvent.AccountClicked -> onAccountSelected(event.accountId)
            AccountsEvent.BackClicked -> onBack()
        }
    }

    fun onBack() {
        onBack.invoke()
    }
}

internal val MOCK_ACCOUNTS = listOf(
    Account(
        id = "kaspi-gold",
        name = "Kaspi Gold",
        type = AccountType.Debit,
        currency = "KZT",
        balance = 425000,
        bank = Bank.Kaspi,
        lastFour = "4429",
    ),
    Account(
        id = "kaspi-red",
        name = "Kaspi Red",
        type = AccountType.Credit,
        currency = "KZT",
        balance = -42000,
        bank = Bank.Kaspi,
        limit = 200000,
    ),
    Account(
        id = "kaspi-deposit",
        name = "Kaspi Deposit",
        type = AccountType.Deposit,
        currency = "KZT",
        balance = 600000,
        bank = Bank.Kaspi,
    ),
    Account(
        id = "halyk-onay",
        name = "Halyk Onay",
        type = AccountType.Debit,
        currency = "USD",
        balance = 850,
        bank = Bank.Halyk,
        baseConv = 406300,
    ),
    Account(
        id = "forte-card",
        name = "Forte Card",
        type = AccountType.Debit,
        currency = "EUR",
        balance = 120,
        bank = Bank.Forte,
        baseConv = 62400,
    ),
    Account(
        id = "wallet",
        name = "Wallet",
        type = AccountType.Cash,
        currency = "KZT",
        balance = 25000,
        bank = Bank.Cash,
    ),
)

private val MOCK_STATE = AccountsUiState(
    totalBalance = "1 476 700",
    totalCurrencyChips = listOf(
        CurrencyChip("KZT", "1.05M"),
        CurrencyChip("USD", "850"),
        CurrencyChip("EUR", "120"),
    ),
    groups = listOf(
        AccountGroup(Bank.Kaspi, MOCK_ACCOUNTS.filter { it.bank == Bank.Kaspi }),
        AccountGroup(Bank.Halyk, MOCK_ACCOUNTS.filter { it.bank == Bank.Halyk }),
        AccountGroup(Bank.Forte, MOCK_ACCOUNTS.filter { it.bank == Bank.Forte }),
        AccountGroup(Bank.Cash, MOCK_ACCOUNTS.filter { it.bank == Bank.Cash }),
    ),
)
