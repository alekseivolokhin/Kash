package com.volokhinaleksey.kash.navigation.importexport

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.presentation.importexport.BankTint
import com.volokhinaleksey.kash.presentation.importexport.ImportPickEvent
import com.volokhinaleksey.kash.presentation.importexport.ImportPickUiState
import com.volokhinaleksey.kash.presentation.importexport.SupportedBank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ImportPickComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val onFilePicked: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(
        ImportPickUiState(
            supportedBanks = listOf(
                SupportedBank("KSP", "Kaspi", "CSV · auto-detect", BankTint.Kaspi),
            ),
        ),
    )
    val uiState: StateFlow<ImportPickUiState> = _uiState

    fun onEvent(event: ImportPickEvent) {
        when (event) {
            ImportPickEvent.BackClicked -> onBack()
            ImportPickEvent.PickFileClicked -> onFilePicked()
            is ImportPickEvent.BankClicked -> Unit
        }
    }

    fun onBackClicked() = onBack()
}
