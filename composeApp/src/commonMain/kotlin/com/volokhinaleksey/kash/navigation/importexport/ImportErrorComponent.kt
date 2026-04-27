package com.volokhinaleksey.kash.navigation.importexport

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.presentation.importexport.ImportErrorEvent
import com.volokhinaleksey.kash.presentation.importexport.ImportErrorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ImportErrorComponent(
    componentContext: ComponentContext,
    initialState: ImportErrorUiState,
    private val onBack: () -> Unit,
    private val onRetry: () -> Unit,
    private val onHelp: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<ImportErrorUiState> = _uiState

    fun onEvent(event: ImportErrorEvent) {
        when (event) {
            ImportErrorEvent.BackClicked -> onBack()
            ImportErrorEvent.RetryClicked -> onRetry()
            ImportErrorEvent.HelpClicked -> onHelp()
        }
    }

    fun onBackClicked() = onBack()
}
