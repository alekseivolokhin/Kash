package com.volokhinaleksey.kash.navigation.accounts

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.accounts.ExchangeRate
import com.volokhinaleksey.kash.presentation.accounts.ExchangeRatesEvent
import com.volokhinaleksey.kash.presentation.accounts.ExchangeRatesUiState
import com.volokhinaleksey.kash.presentation.accounts.RateSource
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.rates_badge_auto
import kash.composeapp.generated.resources.rates_badge_manual
import kash.composeapp.generated.resources.rates_badge_stale
import kash.composeapp.generated.resources.rates_caption
import kash.composeapp.generated.resources.rates_section_active
import kash.composeapp.generated.resources.rates_title
import kash.composeapp.generated.resources.rates_update_action
import kash.composeapp.generated.resources.rates_update_subtitle_format
import kash.composeapp.generated.resources.rates_update_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExchangeRatesScreen(
    component: ExchangeRatesComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    ExchangeRatesContent(
        state = state,
        onBackClick = { component.onEvent(ExchangeRatesEvent.BackClicked) },
        onSyncClick = { component.onEvent(ExchangeRatesEvent.SyncClicked) },
        onRateClick = { component.onEvent(ExchangeRatesEvent.RateClicked(it)) },
        contentPadding = contentPadding,
    )
}

@Composable
private fun ExchangeRatesContent(
    state: ExchangeRatesUiState,
    onBackClick: () -> Unit,
    onSyncClick: () -> Unit,
    onRateClick: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg)
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = contentPadding.calculateBottomPadding() + 32.dp),
        ) {
            KashBackTopBar(
                title = stringResource(Res.string.rates_title),
                onBackClick = onBackClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = KashDimens.ScreenHorizontalPadding, vertical = 12.dp),
            ) {
                Text(
                    text = stringResource(Res.string.rates_caption),
                    color = Kash.colors.sub,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                )

                Spacer(Modifier.height(16.dp))
                SyncBanner(
                    lastSyncLabel = state.lastSyncLabel,
                    onSyncClick = onSyncClick,
                )

                Spacer(Modifier.height(18.dp))
                KashSectionLabel(text = stringResource(Res.string.rates_section_active))
                Spacer(Modifier.height(8.dp))
                RatesList(
                    rates = state.rates,
                    onRateClick = onRateClick,
                )
            }
        }
    }
}

@Composable
private fun SyncBanner(
    lastSyncLabel: String,
    onSyncClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.accentSoft)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val tileBg = if (Kash.colors.isDark) Color(0x387FB089) else Color(0x141F3D2C)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(tileBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.FileDownload,
                contentDescription = null,
                tint = Kash.colors.accentSoftInk,
                modifier = Modifier.size(16.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.rates_update_title),
                color = Kash.colors.accentSoftInk,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(Res.string.rates_update_subtitle_format, lastSyncLabel),
                color = Kash.colors.accentSoftInk.copy(alpha = 0.75f),
                fontSize = 11.5.sp,
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(tileBg)
                .clickable(onClick = onSyncClick)
                .padding(horizontal = 12.dp, vertical = 7.dp),
        ) {
            Text(
                text = stringResource(Res.string.rates_update_action),
                color = Kash.colors.accentSoftInk,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun RatesList(
    rates: List<ExchangeRate>,
    onRateClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp)),
    ) {
        rates.forEachIndexed { idx, rate ->
            RateRow(rate = rate, onClick = { onRateClick(rate.pair) })
            if (idx < rates.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Kash.colors.line),
                )
            }
        }
    }
}

@Composable
private fun RateRow(rate: ExchangeRate, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = rate.pair,
                color = Kash.colors.text,
                style = TextStyle(
                    fontFamily = JetBrainsMonoFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    letterSpacing = (-0.2).sp,
                ),
            )
            Spacer(Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = rate.updated,
                    color = if (rate.stale) Kash.colors.warn else Kash.colors.sub,
                    fontSize = 11.5.sp,
                )
                RateBadge(rate = rate)
            }
        }
        Text(
            text = rate.rate,
            color = Kash.colors.text,
            style = TextStyle(
                fontFamily = JetBrainsMonoFontFamily(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.3).sp,
            ),
        )
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Kash.colors.fade,
            modifier = Modifier.size(15.dp),
        )
    }
}

@Composable
private fun RateBadge(rate: ExchangeRate) {
    val (label, bg, fg) = when {
        rate.source == RateSource.Manual ->
            Triple(stringResource(Res.string.rates_badge_manual), Kash.colors.accentSoft, Kash.colors.accentSoftInk)
        rate.stale ->
            Triple(stringResource(Res.string.rates_badge_stale), Kash.colors.warnSoft, Kash.colors.warn)
        else -> Triple(
            stringResource(Res.string.rates_badge_auto),
            if (Kash.colors.isDark) Kash.colors.cardAlt else Kash.colors.chipBg,
            Kash.colors.sub,
        )
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 1.dp),
    ) {
        Text(
            text = label,
            color = fg,
            style = TextStyle(
                fontFamily = JetBrainsMonoFontFamily(),
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            ),
        )
    }
}

@Preview
@Composable
private fun ExchangeRatesContentPreview() {
    KashTheme {
        ExchangeRatesContent(
            state = ExchangeRatesUiState(
                lastSyncLabel = "today, 09:00",
                rates = listOf(
                    ExchangeRate("USD / KZT", "478.50", RateSource.Auto, "Today, 09:00", false),
                    ExchangeRate("EUR / KZT", "520.20", RateSource.Auto, "Today, 09:00", false),
                    ExchangeRate("RUB / KZT", "5.18", RateSource.Manual, "Yesterday", false),
                    ExchangeRate("GBP / KZT", "605.00", RateSource.Auto, "3 days ago", true),
                ),
            ),
            onBackClick = {},
            onSyncClick = {},
            onRateClick = {},
        )
    }
}
