package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.date_picker_confirm
import kash.composeapp.generated.resources.date_picker_dismiss
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KashDatePickerDialog(
    initialEpochMillis: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(initialEpochMillis = initialEpochMillis)
    val confirmEnabled = remember(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis != null
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DialogTextButton(
                text = stringResource(Res.string.date_picker_confirm),
                color = Kash.colors.accent,
                enabled = confirmEnabled,
                onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) onDateSelected(millis)
                },
            )
        },
        dismissButton = {
            DialogTextButton(
                text = stringResource(Res.string.date_picker_dismiss),
                color = Kash.colors.sub,
                enabled = true,
                onClick = onDismiss,
            )
        },
        colors = kashDatePickerColors(),
        shape = RoundedCornerShape(20.dp),
    ) {
        DatePicker(
            state = datePickerState,
            colors = kashDatePickerColors(),
            showModeToggle = false,
            title = null,
        )
    }
}

@Composable
private fun DialogTextButton(
    text: String,
    color: Color,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (enabled) color else color.copy(alpha = 0.4f),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberDatePickerState(initialEpochMillis: Long) =
    androidx.compose.material3.rememberDatePickerState(
        initialSelectedDateMillis = initialEpochMillis,
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun kashDatePickerColors(): DatePickerColors {
    val c = Kash.colors
    return DatePickerDefaults.colors(
        containerColor = c.card,
        titleContentColor = c.sub,
        headlineContentColor = c.text,
        weekdayContentColor = c.sub,
        subheadContentColor = c.sub,
        navigationContentColor = c.text,
        yearContentColor = c.text,
        currentYearContentColor = c.accent,
        selectedYearContentColor = c.accentInk,
        selectedYearContainerColor = c.accent,
        dayContentColor = c.text,
        disabledDayContentColor = c.fade,
        selectedDayContentColor = c.accentInk,
        disabledSelectedDayContentColor = c.fade,
        selectedDayContainerColor = c.accent,
        disabledSelectedDayContainerColor = c.accent.copy(alpha = 0.4f),
        todayContentColor = c.accent,
        todayDateBorderColor = c.accent,
        dayInSelectionRangeContentColor = c.text,
        dayInSelectionRangeContainerColor = c.accentSoft,
        dividerColor = c.line,
    )
}
