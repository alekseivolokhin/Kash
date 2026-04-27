package com.volokhinaleksey.kash.navigation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.AmountText
import com.volokhinaleksey.kash.components.PeriodFilterChips
import com.volokhinaleksey.kash.components.TransactionsTopBar
import com.volokhinaleksey.kash.components.categorySwatchFor
import com.volokhinaleksey.kash.components.mapCategoryIcon
import com.volokhinaleksey.kash.presentation.stats.MonthlyBarUiModel
import com.volokhinaleksey.kash.presentation.stats.StatsEvent
import com.volokhinaleksey.kash.presentation.stats.StatsUiState
import com.volokhinaleksey.kash.presentation.stats.TopCategoryUiModel
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.expenses
import kash.composeapp.generated.resources.income
import kash.composeapp.generated.resources.stats_empty_action
import kash.composeapp.generated.resources.stats_empty_subtitle
import kash.composeapp.generated.resources.stats_empty_title
import kash.composeapp.generated.resources.stats_overview
import kash.composeapp.generated.resources.stats_spent_less
import kash.composeapp.generated.resources.stats_spent_more
import kash.composeapp.generated.resources.stats_spent_same
import kash.composeapp.generated.resources.stats_title
import kash.composeapp.generated.resources.stats_top_categories
import kash.composeapp.generated.resources.stats_vs_last_month
import org.jetbrains.compose.resources.stringResource

private val HorizontalPadding = 20.dp

@Composable
fun StatsScreen(
    component: StatsComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    val onEvent = remember(component) { component::onEvent }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg)
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        when (val current = state) {
            StatsUiState.Loading -> StatsTopBarSection()
            StatsUiState.Empty -> EmptyStatsContent(
                onAddTransaction = { onEvent(StatsEvent.AddTransactionClicked) },
                bottomPadding = contentPadding.calculateBottomPadding(),
            )
            is StatsUiState.Success -> StatsContent(
                state = current,
                onEvent = onEvent,
                bottomPadding = contentPadding.calculateBottomPadding(),
            )
        }
    }
}

@Composable
private fun StatsContent(
    state: StatsUiState.Success,
    onEvent: (StatsEvent) -> Unit,
    bottomPadding: androidx.compose.ui.unit.Dp,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp, bottom = bottomPadding + 24.dp),
    ) {
        item(key = "topbar") {
            StatsTopBarSection()
            Spacer(Modifier.height(16.dp))
        }
        item(key = "period") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HorizontalPadding),
                contentAlignment = Alignment.CenterStart,
            ) {
                PeriodFilterChips(
                    selectedPeriod = state.selectedPeriod,
                    onPeriodSelected = { onEvent(StatsEvent.PeriodSelected(it)) },
                )
            }
            Spacer(Modifier.height(16.dp))
        }
        item(key = "summary") {
            StatsSummaryRow(
                income = state.income,
                expenses = state.expenses,
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            )
            Spacer(Modifier.height(12.dp))
        }
        item(key = "comparison") {
            ComparisonBanner(
                percent = state.comparisonPercent,
                isSpendingDown = state.isSpendingDown,
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            )
            Spacer(Modifier.height(22.dp))
        }
        item(key = "overview_header") {
            SectionHeader(
                text = stringResource(Res.string.stats_overview),
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            )
            Spacer(Modifier.height(14.dp))
        }
        item(key = "chart") {
            MonthlyChartCard(
                bars = state.monthlyChart,
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            )
            Spacer(Modifier.height(22.dp))
        }
        if (state.topCategories.isNotEmpty()) {
            item(key = "categories_header") {
                SectionHeader(
                    text = stringResource(Res.string.stats_top_categories),
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
                Spacer(Modifier.height(12.dp))
            }
            items(items = state.topCategories, key = { it.id }) { category ->
                TopCategoryRow(
                    category = category,
                    modifier = Modifier
                        .padding(horizontal = HorizontalPadding)
                        .padding(bottom = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun StatsTopBarSection() {
    TransactionsTopBar(title = stringResource(Res.string.stats_title))
}

@Composable
private fun StatsSummaryRow(
    income: String,
    expenses: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        StatsSummaryCard(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.income),
            amount = income,
        )
        StatsSummaryCard(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.expenses),
            amount = expenses,
        )
    }
}

@Composable
private fun StatsSummaryCard(
    label: String,
    amount: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(18.dp))
            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 16.dp),
    ) {
        Text(
            text = label.uppercase(),
            color = Kash.colors.sub,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.2.sp,
        )
        Spacer(Modifier.height(10.dp))
        AmountText(
            amount = amount,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                letterSpacing = (-0.8).sp,
            ),
            color = Kash.colors.text,
            currencyColor = Kash.colors.fade,
            currencyWeight = FontWeight.Normal,
            currencyScale = 14f / 22f,
        )
    }
}

@Composable
private fun ComparisonBanner(
    percent: Int,
    isSpendingDown: Boolean,
    modifier: Modifier = Modifier,
) {
    val c = Kash.colors
    val message = when {
        percent == 0 -> stringResource(Res.string.stats_spent_same)
        isSpendingDown -> stringResource(Res.string.stats_spent_less, percent)
        else -> stringResource(Res.string.stats_spent_more, percent)
    }
    val trendIcon: ImageVector = if (isSpendingDown) Icons.AutoMirrored.Filled.TrendingDown else Icons.AutoMirrored.Filled.TrendingUp
    val arrowIcon: ImageVector = if (isSpendingDown) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward
    val tileBg = if (c.isDark) Color(0x387FB089) else Color(0x141F3D2C)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(c.accentSoft)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(tileBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = trendIcon,
                contentDescription = null,
                tint = c.accentSoftInk,
                modifier = Modifier.size(16.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.stats_vs_last_month),
                color = c.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.2).sp,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = message,
                color = c.sub,
                fontSize = 12.5.sp,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = arrowIcon,
                contentDescription = null,
                tint = c.accentSoftInk,
                modifier = Modifier.size(13.dp),
            )
            Text(
                text = "$percent%",
                color = c.accentSoftInk,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.2).sp,
            )
        }
    }
}

@Composable
private fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = Kash.colors.text,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.3).sp,
        modifier = modifier,
    )
}

@Composable
private fun MonthlyChartCard(
    bars: List<MonthlyBarUiModel>,
    modifier: Modifier = Modifier,
) {
    val c = Kash.colors
    val inactiveBar = if (c.isDark) Color(0x1AFFFFFF) else Color(0x140E1410)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(c.card)
            .border(1.dp, c.line, RoundedCornerShape(18.dp))
            .padding(start = 8.dp, end = 8.dp, top = 20.dp, bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            bars.forEach { bar ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    val safeRatio = bar.ratio.coerceAtLeast(0.04f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(safeRatio)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (bar.isSelected) c.accent else inactiveBar),
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            bars.forEach { bar ->
                Text(
                    text = bar.label,
                    modifier = Modifier.weight(1f),
                    color = if (bar.isSelected) c.text else c.fade,
                    fontSize = 11.sp,
                    fontWeight = if (bar.isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun TopCategoryRow(
    category: TopCategoryUiModel,
    modifier: Modifier = Modifier,
) {
    val c = Kash.colors
    val swatch = categorySwatchFor(category.iconName)
    val trackColor = if (c.isDark) Color(0x0FFFFFFF) else Color(0x0F0E1410)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(c.card)
            .border(1.dp, c.line, RoundedCornerShape(14.dp))
            .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 14.dp),
    ) {
        Row(
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
                    imageVector = mapCategoryIcon(category.iconName),
                    contentDescription = null,
                    tint = swatch.fg,
                    modifier = Modifier.size(16.dp),
                )
            }
            Text(
                text = category.name,
                modifier = Modifier.weight(1f),
                color = c.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
            AmountText(
                amount = category.amount,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    letterSpacing = (-0.2).sp,
                ),
                color = c.text,
                currencyColor = c.fade,
                currencyWeight = FontWeight.Normal,
                currencyScale = 12f / 14f,
            )
        }
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(trackColor),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(category.ratio.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(c.accent),
            )
        }
    }
}

@Composable
private fun EmptyStatsContent(
    onAddTransaction: () -> Unit,
    bottomPadding: androidx.compose.ui.unit.Dp,
) {
    val c = Kash.colors
    val ghostBar = if (c.isDark) Color(0x0DF1ECE0) else Color(0x0D1B1F1A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottomPadding),
    ) {
        StatsTopBarSection()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .widthIn(max = 220.dp)
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(bottom = 0.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                listOf(0.30f, 0.50f, 0.40f, 0.70f, 0.55f, 0.85f).forEach { ratio ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(ratio)
                            .clip(RoundedCornerShape(6.dp))
                            .background(ghostBar)
                            .border(1.dp, c.lineStrong, RoundedCornerShape(6.dp)),
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            Text(
                text = stringResource(Res.string.stats_empty_title),
                color = c.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.5).sp,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.stats_empty_subtitle),
                color = c.sub,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 280.dp),
            )
            Spacer(Modifier.height(22.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(c.accentSoft)
                    .clickable(onClick = onAddTransaction)
                    .padding(horizontal = 18.dp, vertical = 12.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = c.accentSoftInk,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = stringResource(Res.string.stats_empty_action),
                        color = c.accentSoftInk,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
