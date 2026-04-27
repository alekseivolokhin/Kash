package com.volokhinaleksey.kash.navigation.importexport

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.card.KashCard
import com.volokhinaleksey.kash.designsystem.feedback.KashScreenSubtitle
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.field.KashRadio
import com.volokhinaleksey.kash.designsystem.field.KashSwitch
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.importexport.ExportEvent
import com.volokhinaleksey.kash.presentation.importexport.ExportOptions
import com.volokhinaleksey.kash.presentation.importexport.ExportPeriod
import com.volokhinaleksey.kash.presentation.importexport.ExportPeriodOption
import com.volokhinaleksey.kash.presentation.importexport.ExportUiState
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.export_action
import kash.composeapp.generated.resources.export_format_suffix
import kash.composeapp.generated.resources.export_option_group_by_month
import kash.composeapp.generated.resources.export_option_group_by_month_sub
import kash.composeapp.generated.resources.export_option_include_category
import kash.composeapp.generated.resources.export_option_include_category_sub
import kash.composeapp.generated.resources.export_option_include_comment
import kash.composeapp.generated.resources.export_option_include_comment_sub
import kash.composeapp.generated.resources.export_options_label
import kash.composeapp.generated.resources.export_period_all_time
import kash.composeapp.generated.resources.export_period_custom
import kash.composeapp.generated.resources.export_period_label
import kash.composeapp.generated.resources.export_period_last_3_months
import kash.composeapp.generated.resources.export_period_this_month
import kash.composeapp.generated.resources.export_period_this_year
import kash.composeapp.generated.resources.export_screen_subtitle
import kash.composeapp.generated.resources.export_screen_title
import kash.composeapp.generated.resources.export_top_bar_title
import kash.composeapp.generated.resources.export_tx_count_format
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExportScreen(
    component: ExportComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    ExportContent(
        state = state,
        onEvent = component::onEvent,
        onBackClick = component::onBackClicked,
        contentPadding = contentPadding,
    )
}

@Composable
private fun ExportContent(
    state: ExportUiState,
    onEvent: (ExportEvent) -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = contentPadding.calculateTopPadding()),
        ) {
            KashBackTopBar(
                title = stringResource(Res.string.export_top_bar_title),
                onBackClick = onBackClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = KashDimens.ScreenHorizontalPadding)
                    .padding(top = 12.dp, bottom = 110.dp + contentPadding.calculateBottomPadding()),
            ) {
                Text(
                    text = stringResource(Res.string.export_screen_title),
                    color = Kash.colors.text,
                    fontSize = 28.sp,
                    lineHeight = 31.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-1).sp,
                )
                Spacer(Modifier.height(8.dp))
                KashScreenSubtitle(text = stringResource(Res.string.export_screen_subtitle))

                Spacer(Modifier.height(22.dp))
                KashSectionLabel(text = stringResource(Res.string.export_period_label))
                Spacer(Modifier.height(10.dp))
                PeriodList(
                    options = state.periods,
                    selected = state.selectedPeriod,
                    onSelect = { onEvent(ExportEvent.PeriodSelected(it)) },
                )

                Spacer(Modifier.height(22.dp))
                KashSectionLabel(text = stringResource(Res.string.export_options_label))
                Spacer(Modifier.height(10.dp))
                OptionsList(
                    state = state,
                    onEvent = onEvent,
                )

                Spacer(Modifier.height(18.dp))
                FormatBanner(fileName = state.fileName)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = KashDimens.ScreenHorizontalPadding)
                .padding(bottom = 28.dp + contentPadding.calculateBottomPadding()),
        ) {
            KashButton(
                text = stringResource(Res.string.export_action, state.selectedTransactionCount),
                onClick = { onEvent(ExportEvent.ExportClicked) },
                leadingIcon = Icons.Outlined.FileDownload,
            )
        }
    }
}

@Composable
private fun PeriodList(
    options: List<ExportPeriodOption>,
    selected: ExportPeriod,
    onSelect: (ExportPeriod) -> Unit,
) {
    KashCard {
        options.forEachIndexed { index, option ->
            PeriodRow(
                option = option,
                selected = option.period == selected,
                onSelect = { onSelect(option.period) },
            )
            if (index < options.lastIndex) {
                HorizontalDivider(thickness = 1.dp, color = Kash.colors.line)
            }
        }
    }
}

@Composable
private fun PeriodRow(option: ExportPeriodOption, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(horizontal = KashDimens.RowHorizontalPadding, vertical = KashDimens.RowVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        KashRadio(selected = selected)
        Text(
            text = stringResource(option.period.labelRes()),
            color = Kash.colors.text,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
        val count = option.transactionCount
        if (count != null) {
            Text(
                text = stringResource(Res.string.export_tx_count_format, formatTxCount(count)),
                color = Kash.colors.fade,
                fontSize = 12.5.sp,
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Kash.colors.fade,
                modifier = Modifier.size(15.dp),
            )
        }
    }
}

@Composable
private fun OptionsList(state: ExportUiState, onEvent: (ExportEvent) -> Unit) {
    KashCard {
        OptionRow(
            label = stringResource(Res.string.export_option_include_category),
            sub = stringResource(Res.string.export_option_include_category_sub),
            checked = state.options.includeCategory,
            onToggle = { onEvent(ExportEvent.ToggleIncludeCategory) },
        )
        HorizontalDivider(thickness = 1.dp, color = Kash.colors.line)
        OptionRow(
            label = stringResource(Res.string.export_option_include_comment),
            sub = stringResource(Res.string.export_option_include_comment_sub),
            checked = state.options.includeComment,
            onToggle = { onEvent(ExportEvent.ToggleIncludeComment) },
        )
        HorizontalDivider(thickness = 1.dp, color = Kash.colors.line)
        OptionRow(
            label = stringResource(Res.string.export_option_group_by_month),
            sub = stringResource(Res.string.export_option_group_by_month_sub),
            checked = state.options.groupByMonth,
            onToggle = { onEvent(ExportEvent.ToggleGroupByMonth) },
        )
    }
}

@Composable
private fun OptionRow(label: String, sub: String, checked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(horizontal = KashDimens.RowHorizontalPadding, vertical = KashDimens.RowVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = Kash.colors.text,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = sub,
                color = Kash.colors.sub,
                fontSize = 12.sp,
            )
        }
        KashSwitch(checked = checked, onCheckedChange = { onToggle() })
    }
}

@Composable
private fun FormatBanner(fileName: String) {
    val isDark = Kash.colors.isDark
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDark) Kash.colors.cardAlt else Kash.colors.chipBg)
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text(
            text = "$fileName${stringResource(Res.string.export_format_suffix)}",
            color = Kash.colors.sub,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            fontFamily = JetBrainsMonoFontFamily(),
        )
    }
}

private fun ExportPeriod.labelRes() = when (this) {
    ExportPeriod.THIS_MONTH -> Res.string.export_period_this_month
    ExportPeriod.LAST_3_MONTHS -> Res.string.export_period_last_3_months
    ExportPeriod.THIS_YEAR -> Res.string.export_period_this_year
    ExportPeriod.ALL_TIME -> Res.string.export_period_all_time
    ExportPeriod.CUSTOM -> Res.string.export_period_custom
}

private fun formatTxCount(count: Int): String {
    val s = count.toString()
    val sb = StringBuilder()
    val len = s.length
    for (i in 0 until len) {
        if (i > 0 && (len - i) % 3 == 0) sb.append(' ')
        sb.append(s[i])
    }
    return sb.toString()
}

@Preview
@Composable
private fun ExportContentPreview() {
    KashTheme {
        ExportContent(
            state = ExportUiState(
                periods = listOf(
                    ExportPeriodOption(ExportPeriod.THIS_MONTH, 142),
                    ExportPeriodOption(ExportPeriod.LAST_3_MONTHS, 418),
                    ExportPeriodOption(ExportPeriod.THIS_YEAR, 1240),
                    ExportPeriodOption(ExportPeriod.ALL_TIME, 3502),
                    ExportPeriodOption(ExportPeriod.CUSTOM, null),
                ),
                selectedPeriod = ExportPeriod.THIS_MONTH,
                options = ExportOptions(),
                fileName = "kash-export-2026-04.csv",
            ),
            onEvent = {},
            onBackClick = {},
        )
    }
}
