package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.volokhinaleksey.kash.domain.model.Period
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.last_month
import kash.composeapp.generated.resources.quarter
import kash.composeapp.generated.resources.this_month
import org.jetbrains.compose.resources.stringResource

@Composable
fun PeriodFilterChips(
    selectedPeriod: Period,
    onPeriodSelected: (Period) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Period.entries.fastForEach { period ->
            val isSelected = period == selectedPeriod
            val (bgColor, textColor) = if (isSelected) {
                MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh to MaterialTheme.colorScheme.onSurfaceVariant
            }
            Text(
                text = period.displayName,
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .clickable { onPeriodSelected(period) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}

private val Period.displayName: String
    @Composable get() = when (this) {
        Period.THIS_MONTH -> stringResource(Res.string.this_month)
        Period.LAST_MONTH -> stringResource(Res.string.last_month)
        Period.QUARTER -> stringResource(Res.string.quarter)
    }
