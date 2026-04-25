package com.volokhinaleksey.kash.presentation.importexport

enum class ExportPeriod {
    THIS_MONTH,
    LAST_3_MONTHS,
    THIS_YEAR,
    ALL_TIME,
    CUSTOM,
}

data class ExportPeriodOption(
    val period: ExportPeriod,
    val transactionCount: Int?,
)

data class ExportOptions(
    val includeCategory: Boolean = true,
    val includeComment: Boolean = true,
    val groupByMonth: Boolean = false,
)

data class ExportUiState(
    val periods: List<ExportPeriodOption>,
    val selectedPeriod: ExportPeriod,
    val options: ExportOptions,
    val fileName: String,
) {
    val selectedTransactionCount: Int
        get() = periods.firstOrNull { it.period == selectedPeriod }?.transactionCount ?: 0
}

sealed interface ExportEvent {
    data object BackClicked : ExportEvent
    data class PeriodSelected(val period: ExportPeriod) : ExportEvent
    data object ToggleIncludeCategory : ExportEvent
    data object ToggleIncludeComment : ExportEvent
    data object ToggleGroupByMonth : ExportEvent
    data object ExportClicked : ExportEvent
}
