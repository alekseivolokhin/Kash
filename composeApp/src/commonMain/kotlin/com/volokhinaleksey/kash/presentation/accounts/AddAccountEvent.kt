package com.volokhinaleksey.kash.presentation.accounts

sealed interface AddAccountEvent {
    data object BackClicked : AddAccountEvent
    data class TypeSelected(val type: AccountType) : AddAccountEvent
    data class BalanceChanged(val balance: String) : AddAccountEvent
    data class NameChanged(val name: String) : AddAccountEvent
    data object InstitutionClicked : AddAccountEvent
    data object CurrencyClicked : AddAccountEvent
    data class ColorSelected(val index: Int) : AddAccountEvent
    data object CreateClicked : AddAccountEvent
}
