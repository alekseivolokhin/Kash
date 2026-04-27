package com.volokhinaleksey.kash.presentation.importexport

data class ImportErrorUiState(
    val fileName: String,
    val errorCode: String,
    val pages: Int,
    val sizeLabel: String,
)

sealed interface ImportErrorEvent {
    data object BackClicked : ImportErrorEvent
    data object RetryClicked : ImportErrorEvent
    data object HelpClicked : ImportErrorEvent
}
