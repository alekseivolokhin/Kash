package com.volokhinaleksey.kash.navigation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.components.TransactionGroupCard
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.chip.KashFilterChip
import com.volokhinaleksey.kash.designsystem.field.KashSearchField
import com.volokhinaleksey.kash.designsystem.topbar.KashLogoTopBar
import com.volokhinaleksey.kash.presentation.transactions.TransactionGroupLabel
import com.volokhinaleksey.kash.presentation.transactions.TransactionGroupUiModel
import com.volokhinaleksey.kash.presentation.transactions.TransactionRowUiModel
import com.volokhinaleksey.kash.presentation.transactions.TransactionsEvent
import com.volokhinaleksey.kash.presentation.transactions.TransactionsFilter
import com.volokhinaleksey.kash.presentation.transactions.TransactionsUiState
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.add_transaction
import kash.composeapp.generated.resources.transactions_empty
import kash.composeapp.generated.resources.transactions_filter_account
import kash.composeapp.generated.resources.transactions_filter_all
import kash.composeapp.generated.resources.transactions_filter_expense
import kash.composeapp.generated.resources.transactions_filter_income
import kash.composeapp.generated.resources.transactions_filter_transfer
import kash.composeapp.generated.resources.transactions_search_hint
import kash.composeapp.generated.resources.transactions_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TransactionsScreen(
    component: TransactionsComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    TransactionsContent(
        state = state,
        onEvent = component::onEvent,
        contentPadding = contentPadding,
    )
}

@Composable
private fun TransactionsContent(
    state: TransactionsUiState,
    onEvent: (TransactionsEvent) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding() + 96.dp,
            ),
        ) {
            item(key = "topbar") {
                KashLogoTopBar(largeTitle = stringResource(Res.string.transactions_title))
                Spacer(Modifier.height(16.dp))
            }
            item(key = "search") {
                KashSearchField(
                    value = state.searchQuery,
                    onValueChange = { onEvent(TransactionsEvent.SearchQueryChanged(it)) },
                    placeholder = stringResource(Res.string.transactions_search_hint),
                    modifier = Modifier.padding(horizontal = KashDimens.ScreenHorizontalPadding),
                )
                Spacer(Modifier.height(12.dp))
            }
            item(key = "chips") {
                TransactionsFiltersRow(
                    selected = state.filter,
                    onFilterSelected = { onEvent(TransactionsEvent.FilterSelected(it)) },
                    categoryFilters = state.availableCategoryFilters,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = KashDimens.ScreenHorizontalPadding),
                )
                Spacer(Modifier.height(8.dp))
            }

            when (state) {
                is TransactionsUiState.Loading -> Unit
                is TransactionsUiState.Content -> {
                    if (state.isEmpty) {
                        item(key = "empty") {
                            EmptyState(modifier = Modifier.padding(top = 64.dp))
                        }
                    } else {
                        items(
                            items = state.groups,
                            key = { it.key },
                        ) { group ->
                            TransactionGroupCard(
                                label = group.label,
                                items = group.items,
                                modifier = Modifier
                                    .padding(horizontal = KashDimens.ScreenHorizontalPadding, vertical = 9.dp),
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { onEvent(TransactionsEvent.AddTransactionClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = KashDimens.ScreenHorizontalPadding,
                    bottom = contentPadding.calculateBottomPadding() + 20.dp,
                )
                .size(52.dp),
            shape = RoundedCornerShape(16.dp),
            containerColor = Kash.colors.accent,
            contentColor = Kash.colors.accentInk,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.add_transaction),
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun TransactionsFiltersRow(
    selected: TransactionsFilter,
    onFilterSelected: (TransactionsFilter) -> Unit,
    categoryFilters: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KashFilterChip(
            label = stringResource(Res.string.transactions_filter_all),
            selected = selected is TransactionsFilter.All,
            onClick = { onFilterSelected(TransactionsFilter.All) },
        )
        KashFilterChip(
            label = stringResource(Res.string.transactions_filter_income),
            selected = selected is TransactionsFilter.Income,
            onClick = { onFilterSelected(TransactionsFilter.Income) },
        )
        KashFilterChip(
            label = stringResource(Res.string.transactions_filter_expense),
            selected = selected is TransactionsFilter.Expense,
            onClick = { onFilterSelected(TransactionsFilter.Expense) },
        )
        KashFilterChip(
            label = stringResource(Res.string.transactions_filter_transfer),
            selected = selected is TransactionsFilter.Transfer,
            onClick = { onFilterSelected(TransactionsFilter.Transfer) },
        )
        KashFilterChip(
            label = stringResource(Res.string.transactions_filter_account),
            selected = selected is TransactionsFilter.Account,
            onClick = { onFilterSelected(TransactionsFilter.Account) },
            trailingCount = 2,
        )
        categoryFilters.forEach { category ->
            KashFilterChip(
                label = category,
                selected = selected is TransactionsFilter.Category &&
                    selected.name.equals(category, ignoreCase = true),
                onClick = { onFilterSelected(TransactionsFilter.Category(category)) },
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.transactions_empty),
            color = Kash.colors.sub,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        )
    }
}

@Preview
@Composable
private fun TransactionsContentEmptyPreview() {
    KashTheme {
        TransactionsContent(
            state = TransactionsUiState.Content(
                searchQuery = "",
                filter = TransactionsFilter.All,
                availableCategoryFilters = emptyList(),
                groups = emptyList(),
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun TransactionsContentSuccessPreview() {
    KashTheme {
        TransactionsContent(
            state = TransactionsUiState.Content(
                searchQuery = "",
                filter = TransactionsFilter.All,
                availableCategoryFilters = listOf("Food", "Transport"),
                groups = listOf(
                    TransactionGroupUiModel(
                        key = "today",
                        label = TransactionGroupLabel.Today,
                        items = listOf(
                            TransactionRowUiModel(
                                id = 1,
                                name = "NoMad Kitchen",
                                category = "Food",
                                time = "12:34",
                                amount = "−12 400 ₸",
                                isIncome = false,
                                iconName = "restaurant",
                            ),
                        ),
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}
