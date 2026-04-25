package com.volokhinaleksey.kash.presentation.importexport

data class ImportDetectedSource(
    val bankCode: String,
    val bankName: String,
    val fileName: String,
    val transactionCount: Int,
    val rangeLabel: String,
)

data class ImportRow(
    val id: Long,
    val dateLabel: String,
    val merchant: String,
    val categoryName: String,
    val categoryIconName: String,
    val amountCents: Long,
    val needsReview: Boolean,
)

data class ImportPreviewUiState(
    val source: ImportDetectedSource,
    val newCount: Int,
    val duplicateCount: Int,
    val needReviewCount: Int,
    val rows: List<ImportRow>,
)

sealed interface ImportPreviewEvent {
    data object BackClicked : ImportPreviewEvent
    data object CancelClicked : ImportPreviewEvent
    data object ImportClicked : ImportPreviewEvent
    data class RowClicked(val id: Long) : ImportPreviewEvent
}
