package com.volokhinaleksey.kash.presentation.importexport

data class SupportedBank(
    val code: String,
    val name: String,
    val sourceLabel: String,
    val tint: BankTint,
)

enum class BankTint { Kaspi }

data class ImportPickUiState(
    val supportedBanks: List<SupportedBank>,
)

sealed interface ImportPickEvent {
    data object BackClicked : ImportPickEvent
    data object PickFileClicked : ImportPickEvent
    data class BankClicked(val code: String) : ImportPickEvent
}
