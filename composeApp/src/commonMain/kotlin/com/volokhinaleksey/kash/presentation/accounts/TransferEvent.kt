package com.volokhinaleksey.kash.presentation.accounts

sealed interface TransferEvent {
    data object BackClicked : TransferEvent
    data object FromClicked : TransferEvent
    data object ToClicked : TransferEvent
    data class SendAmountChanged(val value: String) : TransferEvent
    data object RateOverrideClicked : TransferEvent
    data class CommentChanged(val value: String) : TransferEvent
    data object SaveClicked : TransferEvent
}
