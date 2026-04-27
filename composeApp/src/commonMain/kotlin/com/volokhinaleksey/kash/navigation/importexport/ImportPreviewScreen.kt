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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.categorySwatchFor
import com.volokhinaleksey.kash.components.mapCategoryIcon
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.button.KashButtonVariant
import com.volokhinaleksey.kash.designsystem.card.KashCard
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.importexport.ImportDetectedSource
import com.volokhinaleksey.kash.presentation.importexport.ImportPreviewEvent
import com.volokhinaleksey.kash.presentation.importexport.ImportPreviewUiState
import com.volokhinaleksey.kash.presentation.importexport.ImportRow
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import com.volokhinaleksey.kash.theme.NumericTextStyle
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.cancel
import kash.composeapp.generated.resources.import_action
import kash.composeapp.generated.resources.import_pick_category
import kash.composeapp.generated.resources.import_preview_top_bar_title
import kash.composeapp.generated.resources.import_stat_duplicates
import kash.composeapp.generated.resources.import_stat_need_review
import kash.composeapp.generated.resources.import_stat_new
import kash.composeapp.generated.resources.import_transactions_count_format
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ImportPreviewScreen(
    component: ImportPreviewComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    ImportPreviewContent(
        state = state,
        onEvent = component::onEvent,
        onBackClick = component::onBackClicked,
        contentPadding = contentPadding,
    )
}

@Composable
private fun ImportPreviewContent(
    state: ImportPreviewUiState,
    onEvent: (ImportPreviewEvent) -> Unit,
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
                title = stringResource(Res.string.import_preview_top_bar_title),
                onBackClick = onBackClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = KashDimens.ScreenHorizontalPadding)
                    .padding(top = 12.dp, bottom = 110.dp + contentPadding.calculateBottomPadding()),
            ) {
                DetectionBanner(state)

                Spacer(Modifier.height(12.dp))
                StatsRow(
                    new = state.newCount,
                    duplicates = state.duplicateCount,
                    needReview = state.needReviewCount,
                )

                Spacer(Modifier.height(16.dp))
                KashCard {
                    state.rows.forEachIndexed { index, row ->
                        PreviewRow(
                            row = row,
                            onClick = { onEvent(ImportPreviewEvent.RowClicked(row.id)) },
                        )
                        if (index < state.rows.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Kash.colors.line,
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = KashDimens.ScreenHorizontalPadding)
                .padding(bottom = 28.dp + contentPadding.calculateBottomPadding()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            KashButton(
                text = stringResource(Res.string.cancel),
                onClick = { onEvent(ImportPreviewEvent.CancelClicked) },
                variant = KashButtonVariant.Secondary,
                modifier = Modifier.weight(1f),
            )
            KashButton(
                text = stringResource(Res.string.import_action, state.newCount),
                onClick = { onEvent(ImportPreviewEvent.ImportClicked) },
                modifier = Modifier.weight(2f),
            )
        }
    }
}

@Composable
private fun DetectionBanner(state: ImportPreviewUiState) {
    val codeBg = if (Kash.colors.isDark) Color(0x405A8C66) else Color(0x1A1F3D2C)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.accentSoft)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(codeBg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = state.source.bankCode,
                color = Kash.colors.accentSoftInk,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${state.source.bankName} · ${state.source.fileName}",
                color = Kash.colors.accentSoftInk,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = stringResource(
                    Res.string.import_transactions_count_format,
                    state.source.transactionCount,
                    state.source.rangeLabel,
                ),
                color = Kash.colors.accentSoftInk.copy(alpha = 0.75f),
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun StatsRow(new: Int, duplicates: Int, needReview: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatCard(
            label = stringResource(Res.string.import_stat_new),
            value = new.toString(),
            warn = false,
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = stringResource(Res.string.import_stat_duplicates),
            value = duplicates.toString(),
            warn = false,
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = stringResource(Res.string.import_stat_need_review),
            value = needReview.toString(),
            warn = needReview > 0,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, warn: Boolean, modifier: Modifier = Modifier) {
    KashCard(modifier = modifier, radius = 12.dp) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            KashSectionLabel(text = label)
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                color = if (warn) Kash.colors.neg else Kash.colors.text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.4).sp,
            )
        }
    }
}

@Composable
private fun PreviewRow(row: ImportRow, onClick: () -> Unit) {
    val swatch = categorySwatchFor(row.categoryIconName)
    val isDark = Kash.colors.isDark
    val rowBg = when {
        row.needsReview && isDark -> Color(0x0FD27F73)
        row.needsReview -> Color(0x0AB0473A)
        else -> Color.Transparent
    }
    val isPositive = row.amountCents > 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(swatch.bg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = mapCategoryIcon(row.categoryIconName),
                contentDescription = null,
                tint = swatch.fg,
                modifier = Modifier.size(15.dp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = row.merchant,
                color = Kash.colors.text,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.1).sp,
                maxLines = 1,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = if (row.needsReview) {
                    "⚠ " + stringResource(Res.string.import_pick_category)
                } else {
                    "${row.categoryName} · ${row.dateLabel}"
                },
                color = if (row.needsReview) Kash.colors.neg else Kash.colors.sub,
                fontSize = 11.5.sp,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = (if (isPositive) "+" else "−") + formatAmountCents(row.amountCents),
                color = if (isPositive) Kash.colors.pos else Kash.colors.text,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.2).sp,
                style = NumericTextStyle.copy(fontWeight = FontWeight.SemiBold),
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = "₸",
                color = Kash.colors.fade,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

private fun formatAmountCents(amountCents: Long): String {
    val abs = kotlin.math.abs(amountCents)
    val whole = abs / 100
    val s = whole.toString()
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
private fun ImportPreviewContentPreview() {
    KashTheme {
        ImportPreviewContent(
            state = ImportPreviewUiState(
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
                rows = listOf(
                    ImportRow(1, "Oct 12", "Apple Store", "Electronics", "computer", -59_900, false),
                    ImportRow(2, "Oct 12", "NoMad Kitchen", "Food", "restaurant", -12_400, false),
                    ImportRow(3, "Oct 11", "Yandex Taxi", "Transport", "directions_car", -8_200, false),
                    ImportRow(4, "Oct 11", "IT Solutions LLC", "Income", "work", 145_000, false),
                ),
            ),
            onEvent = {},
            onBackClick = {},
        )
    }
}
