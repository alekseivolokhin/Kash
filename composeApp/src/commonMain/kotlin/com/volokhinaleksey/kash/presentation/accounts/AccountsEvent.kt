package com.volokhinaleksey.kash.presentation.accounts

sealed interface AccountsEvent {
    data object AddAccountClicked : AccountsEvent
    data object ImportStatementClicked : AccountsEvent
    data class AccountClicked(val accountId: String) : AccountsEvent
    data object BackClicked : AccountsEvent
}
