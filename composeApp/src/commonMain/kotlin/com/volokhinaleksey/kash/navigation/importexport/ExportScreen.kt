package com.volokhinaleksey.kash.navigation.importexport

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.BackTopBar
import com.volokhinaleksey.kash.presentation.importexport.ExportEvent
import com.volokhinaleksey.kash.presentation.importexport.ExportPeriod
import com.volokhinaleksey.kash.presentation.importexport.ExportPeriodOption
import com.volokhinaleksey.kash.presentation.importexport.ExportUiState
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
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

@Composable
fun ExportScreen(
    component: ExportComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    ExportContent(
        state = state,
        onEvent = remember(component) { component::onEvent },
        onBackClick = remember(component) { component::onBackClicked },
        contentPadding = contentPadding,
    )
}

@Composable
private fun ExportContent(
    state: ExportUiState,
    onEvent: (ExportEvent) -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
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
            BackTopBar(
                title = stringResource(Res.string.export_top_bar_title),
                onBackClick = onBackClick,
                showNotifications = false,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
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
                Text(
                    text = stringResource(Res.string.export_screen_subtitle),
                    color = Kash.colors.sub,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                )

                Spacer(Modifier.height(22.dp))
                SectionLabel(text = stringResource(Res.string.export_period_label))
                Spacer(Modifier.height(10.dp))
                PeriodList(
                    options = state.periods,
                    selected = state.selectedPeriod,
                    onSelect = { onEvent(ExportEvent.PeriodSelected(it)) },
                )

                Spacer(Modifier.height(22.dp))
                SectionLabel(text = stringResource(Res.string.export_options_label))
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
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp + contentPadding.calculateBottomPadding()),
        ) {
            DownloadCta(
                count = state.selectedTransactionCount,
                onClick = { onEvent(ExportEvent.ExportClicked) },
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = Kash.colors.sub,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.4.sp,
    )
}

@Composable
private fun PeriodList(
    options: List<ExportPeriodOption>,
    selected: ExportPeriod,
    onSelect: (ExportPeriod) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp)),
    ) {
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
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Radio(selected = selected)
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
private fun Radio(selected: Boolean) {
    val borderColor = if (selected) Kash.colors.accent else Kash.colors.lineStrong
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(if (selected) Kash.colors.accent else Color.Transparent)
            .border(1.5.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(Kash.colors.accentInk),
            )
        }
    }
}

@Composable
private fun OptionsList(state: ExportUiState, onEvent: (ExportEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp)),
    ) {
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
            .padding(horizontal = 16.dp, vertical = 14.dp),
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
        ToggleSwitch(checked = checked)
    }
}

@Composable
private fun ToggleSwitch(checked: Boolean) {
    val isDark = Kash.colors.isDark
    val offTrack = if (isDark) Kash.colors.cardAlt else Kash.colors.chipBg
    val trackColor by animateColorAsState(
        targetValue = if (checked) Kash.colors.accent else offTrack,
        animationSpec = tween(durationMillis = 150),
        label = "toggleTrack",
    )
    val thumbX by animateDpAsState(
        targetValue = if (checked) 18.dp else 2.dp,
        animationSpec = tween(durationMillis = 150),
        label = "toggleThumbX",
    )
    val thumbColor = if (checked) Kash.colors.accentInk else Color.White

    Box(
        modifier = Modifier
            .width(38.dp)
            .height(22.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(trackColor)
            .let { base ->
                if (!checked) base.border(1.dp, Kash.colors.line, RoundedCornerShape(999.dp))
                else base
            },
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbX, y = 2.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(thumbColor),
        )
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

@Composable
private fun DownloadCta(count: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.accent)
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.FileDownload,
            contentDescription = null,
            tint = Kash.colors.accentInk,
            modifier = Modifier.size(17.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(Res.string.export_action, count),
            color = Kash.colors.accentInk,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.2).sp,
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
