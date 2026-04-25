package com.volokhinaleksey.kash.navigation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.components.EmptyHomeContent
import com.volokhinaleksey.kash.components.HomeTopBar
import com.volokhinaleksey.kash.components.IncomeExpenseRow
import com.volokhinaleksey.kash.components.PeriodFilterChips
import com.volokhinaleksey.kash.components.RecentTransactionsHeader
import com.volokhinaleksey.kash.components.TotalBalanceCard
import com.volokhinaleksey.kash.components.TransactionItem
import com.volokhinaleksey.kash.presentation.home.HomeEvent
import com.volokhinaleksey.kash.presentation.home.HomeUiState
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.add_transaction
import org.jetbrains.compose.resources.stringResource

private val HorizontalPadding = 20.dp

@Composable
fun HomeScreen(
    component: HomeComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    when (val state = component.uiState.collectAsState().value) {
        is HomeUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Kash.colors.bg),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Kash.colors.accent)
            }
        }

        is HomeUiState.Empty -> EmptyHomeScreenContent(contentPadding, component)

        is HomeUiState.Success -> HomeScreenContent(contentPadding, state, component)
    }
}

@Composable
private fun EmptyHomeScreenContent(
    contentPadding: PaddingValues,
    component: HomeComponent,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
            ),
    ) {
        HomeTopBar()
        EmptyHomeContent(
            onAddTransactionClick = remember(component) {
                { component.onEvent(HomeEvent.AddTransactionClicked) }
            },
            onImportStatementClick = remember(component) {
                { component.onEvent(HomeEvent.ImportStatementClicked) }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun HomeScreenContent(
    contentPadding: PaddingValues,
    state: HomeUiState.Success,
    component: HomeComponent,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 8.dp,
                bottom = contentPadding.calculateBottomPadding() + 96.dp,
            ),
        ) {
            item(key = "header") {
                HomeTopBar()
                Spacer(Modifier.height(20.dp))
            }
            item(key = "total_balance") {
                TotalBalanceCard(
                    totalBalance = state.totalBalance,
                    percentChange = state.percentChange,
                    isPositiveChange = state.isPositiveChange,
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
                Spacer(Modifier.height(16.dp))
            }
            item(key = "income_expense") {
                IncomeExpenseRow(
                    income = state.income,
                    expenses = state.expenses,
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
                Spacer(Modifier.height(20.dp))
            }
            item(key = "select_period") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = HorizontalPadding),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    PeriodFilterChips(
                        selectedPeriod = state.selectedPeriod,
                        onPeriodSelected = remember(component) {
                            { component.onEvent(HomeEvent.PeriodSelected(it)) }
                        },
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
            item(key = "recent_transactions") {
                RecentTransactionsHeader(
                    onViewAllClick = remember(component) {
                        { component.onEvent(HomeEvent.ViewAllTransactionsClicked) }
                    },
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
                Spacer(Modifier.height(2.dp))
            }
            itemsIndexed(
                items = state.recentTransactions,
                key = { _, item -> item.id },
            ) { index, transaction ->
                TransactionItem(
                    name = transaction.name,
                    category = "${transaction.category} · ${transaction.date}",
                    amount = transaction.amount,
                    isIncome = transaction.isIncome,
                    iconName = transaction.iconName,
                    showDivider = index < state.recentTransactions.lastIndex,
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
            }
        }

        FloatingActionButton(
            onClick = { component.onEvent(HomeEvent.AddTransactionClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = HorizontalPadding,
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
            )
        }
    }
}
