package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import com.volokhinaleksey.kash.presentation.transactions.TransactionsFilter
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.transactions_filter_all
import kash.composeapp.generated.resources.transactions_filter_expense
import kash.composeapp.generated.resources.transactions_filter_income
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionsFilterChips(
    selected: TransactionsFilter,
    onFilterSelected: (TransactionsFilter) -> Unit,
    categoryFilters: List<String>,
    modifier: Modifier = Modifier,
) {
    val all = stringResource(Res.string.transactions_filter_all)
    val income = stringResource(Res.string.transactions_filter_income)
    val expense = stringResource(Res.string.transactions_filter_expense)

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilterChip(
            label = all,
            selected = selected is TransactionsFilter.All,
            onClick = { onFilterSelected(TransactionsFilter.All) },
        )
        FilterChip(
            label = income,
            selected = selected is TransactionsFilter.Income,
            onClick = { onFilterSelected(TransactionsFilter.Income) },
        )
        FilterChip(
            label = expense,
            selected = selected is TransactionsFilter.Expense,
            onClick = { onFilterSelected(TransactionsFilter.Expense) },
        )
        categoryFilters.forEach { category ->
            FilterChip(
                label = category,
                selected = selected is TransactionsFilter.Category && selected.name.equals(category, ignoreCase = true),
                onClick = { onFilterSelected(TransactionsFilter.Category(category)) },
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(50)
    val background = if (selected) Kash.colors.accent else Color.Transparent
    val borderColor = if (selected) Color.Transparent else Kash.colors.line
    val contentColor = if (selected) Kash.colors.accentInk else Kash.colors.sub

    Box(
        modifier = Modifier
            .clip(shape)
            .background(background)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 12.5.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            ),
        )
    }
}
