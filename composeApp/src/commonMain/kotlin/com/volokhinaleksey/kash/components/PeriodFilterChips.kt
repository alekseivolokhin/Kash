package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.volokhinaleksey.kash.domain.model.Period
import com.volokhinaleksey.kash.theme.Kash
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
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Kash.colors.chipBg)
            .padding(3.dp),
    ) {
        Period.entries.fastForEach { period ->
            val isSelected = period == selectedPeriod
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) Kash.colors.card else Color.Transparent)
                    .clickable { onPeriodSelected(period) }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = period.displayName,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        fontSize = 13.sp,
                        letterSpacing = (-0.1).sp,
                    ),
                    color = if (isSelected) Kash.colors.text else Kash.colors.sub,
                )
            }
        }
    }
}

private val Period.displayName: String
    @Composable get() = when (this) {
        Period.THIS_MONTH -> stringResource(Res.string.this_month)
        Period.LAST_MONTH -> stringResource(Res.string.last_month)
        Period.QUARTER -> stringResource(Res.string.quarter)
    }
