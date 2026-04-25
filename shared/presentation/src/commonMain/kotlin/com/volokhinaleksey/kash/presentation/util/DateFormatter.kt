package com.volokhinaleksey.kash.presentation.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

private val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

fun formatDateShort(epochMillis: Long): String {
    val date = kotlin.time.Instant.fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "${monthNames[date.month.number - 1]} ${date.day}"
}

fun formatDateLong(epochMillis: Long): String {
    val date = kotlin.time.Instant.fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "${date.day} ${monthNames[date.month.number - 1]} ${date.year}"
}

fun formatTimeOfDay(epochMillis: Long): String {
    val dt = kotlin.time.Instant.fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dt.hour.toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}"
}

sealed interface DayLabel {
    data object Today : DayLabel
    data object Yesterday : DayLabel
    data class Date(val text: String) : DayLabel
}

fun dayLabelFor(
    epochMillis: Long,
    today: LocalDate = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    tz: TimeZone = TimeZone.currentSystemDefault(),
): DayLabel {
    val date = kotlin.time.Instant.fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(tz).date
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    return when (date) {
        today -> DayLabel.Today
        yesterday -> DayLabel.Yesterday
        else -> {
            val month = monthNames[date.month.number - 1]
            val text = if (date.year == today.year) "$month ${date.day}"
            else "$month ${date.day}, ${date.year}"
            DayLabel.Date(text)
        }
    }
}

fun startOfDayMillis(
    epochMillis: Long,
    tz: TimeZone = TimeZone.currentSystemDefault(),
): Long {
    val date = kotlin.time.Instant.fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(tz).date
    return date.atStartOfDayIn(tz).toEpochMilliseconds()
}
