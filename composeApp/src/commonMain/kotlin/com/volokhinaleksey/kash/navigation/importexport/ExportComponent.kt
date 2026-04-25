package com.volokhinaleksey.kash.navigation.importexport

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.presentation.importexport.ExportEvent
import com.volokhinaleksey.kash.presentation.importexport.ExportOptions
import com.volokhinaleksey.kash.presentation.importexport.ExportPeriod
import com.volokhinaleksey.kash.presentation.importexport.ExportPeriodOption
import com.volokhinaleksey.kash.presentation.importexport.ExportUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ExportComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(
        ExportUiState(
            periods = listOf(
                ExportPeriodOption(ExportPeriod.THIS_MONTH, 142),
                ExportPeriodOption(ExportPeriod.LAST_3_MONTHS, 418),
                ExportPeriodOption(ExportPeriod.THIS_YEAR, 1_240),
                ExportPeriodOption(ExportPeriod.ALL_TIME, 3_502),
                ExportPeriodOption(ExportPeriod.CUSTOM, null),
            ),
            selectedPeriod = ExportPeriod.THIS_MONTH,
            options = ExportOptions(),
            fileName = "kash-export-2026-04.csv",
        ),
    )
    val uiState: StateFlow<ExportUiState> = _uiState

    fun onEvent(event: ExportEvent) {
        when (event) {
            ExportEvent.BackClicked -> onBack()
            is ExportEvent.PeriodSelected ->
                _uiState.update { it.copy(selectedPeriod = event.period) }
            ExportEvent.ToggleIncludeCategory ->
                _uiState.update { it.copy(options = it.options.copy(includeCategory = !it.options.includeCategory)) }
            ExportEvent.ToggleIncludeComment ->
                _uiState.update { it.copy(options = it.options.copy(includeComment = !it.options.includeComment)) }
            ExportEvent.ToggleGroupByMonth ->
                _uiState.update { it.copy(options = it.options.copy(groupByMonth = !it.options.groupByMonth)) }
            ExportEvent.ExportClicked -> onBack()
        }
    }

    fun onBackClicked() = onBack()
}
