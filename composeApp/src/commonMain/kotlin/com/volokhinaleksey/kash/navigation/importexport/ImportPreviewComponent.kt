package com.volokhinaleksey.kash.navigation.importexport

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.presentation.importexport.ImportDetectedSource
import com.volokhinaleksey.kash.presentation.importexport.ImportPreviewEvent
import com.volokhinaleksey.kash.presentation.importexport.ImportPreviewUiState
import com.volokhinaleksey.kash.presentation.importexport.ImportRow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ImportPreviewComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(
        ImportPreviewUiState(
            source = ImportDetectedSource(
                bankCode = "TIN",
                bankName = "Tinkoff",
                fileName = "operations.csv",
                transactionCount = 26,
                rangeLabel = "Oct 1 – Oct 12",
            ),
            newCount = 23,
            duplicateCount = 3,
            needReviewCount = 1,
            rows = sampleRows,
        ),
    )
    val uiState: StateFlow<ImportPreviewUiState> = _uiState

    fun onEvent(event: ImportPreviewEvent) {
        when (event) {
            ImportPreviewEvent.BackClicked,
            ImportPreviewEvent.CancelClicked,
            ImportPreviewEvent.ImportClicked -> onBack()
            is ImportPreviewEvent.RowClicked -> Unit
        }
    }

    fun onBackClicked() = onBack()
}

private val sampleRows = listOf(
    ImportRow(1, "Oct 12", "Apple Store", "Electronics", "computer", -59_900, false),
    ImportRow(2, "Oct 12", "NoMad Kitchen", "Food", "restaurant", -12_400, false),
    ImportRow(3, "Oct 11", "Yandex Taxi", "Transport", "directions_car", -8_200, false),
    ImportRow(4, "Oct 11", "IT Solutions LLC", "Income", "work", 145_000, false),
    ImportRow(5, "Oct 10", "KASPI 4444", "Uncategorized", "computer", -5_400, true),
    ImportRow(6, "Oct 10", "Spotify Premium", "Subscriptions", "subscriptions", -2_200, false),
)
