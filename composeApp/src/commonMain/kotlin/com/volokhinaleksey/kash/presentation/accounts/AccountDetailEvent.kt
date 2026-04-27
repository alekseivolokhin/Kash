package com.volokhinaleksey.kash.presentation.accounts

sealed interface AccountDetailEvent {
    data object BackClicked : AccountDetailEvent
    data object MoreClicked : AccountDetailEvent
    data class FilterSelected(val filter: AccountDetailFilter) : AccountDetailEvent
    data object MakeTransferClicked : AccountDetailEvent
}
