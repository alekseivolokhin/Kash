package com.volokhinaleksey.kash.presentation.accounts

import com.volokhinaleksey.kash.designsystem.bank.Bank

sealed interface InstitutionPickerEvent {
    data object BackClicked : InstitutionPickerEvent
    data class SearchQueryChanged(val query: String) : InstitutionPickerEvent
    data class BankSelected(val bank: Bank) : InstitutionPickerEvent
    data object NoBankClicked : InstitutionPickerEvent
}
