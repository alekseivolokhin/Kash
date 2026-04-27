package com.volokhinaleksey.kash.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.volokhinaleksey.kash.designsystem.chip.KashPillItem
import com.volokhinaleksey.kash.designsystem.chip.KashPillSwitch
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
    KashPillSwitch(
        items = listOf(
            KashPillItem(Period.THIS_MONTH, stringResource(Res.string.this_month)),
            KashPillItem(Period.LAST_MONTH, stringResource(Res.string.last_month)),
            KashPillItem(Period.QUARTER, stringResource(Res.string.quarter)),
        ),
        selected = selectedPeriod,
        onSelected = onPeriodSelected,
        modifier = modifier,
    )
}
