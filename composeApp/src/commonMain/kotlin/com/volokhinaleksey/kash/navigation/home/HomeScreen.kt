package com.volokhinaleksey.kash.navigation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.components.HomeTopBar
import com.volokhinaleksey.kash.components.IncomeExpenseRow
import com.volokhinaleksey.kash.components.PeriodFilterChips
import com.volokhinaleksey.kash.components.RecentTransactionsHeader
import com.volokhinaleksey.kash.components.TotalBalanceCard
import com.volokhinaleksey.kash.components.TransactionItem
import com.volokhinaleksey.kash.components.mapCategoryIcon
import com.volokhinaleksey.kash.presentation.home.HomeEvent
import com.volokhinaleksey.kash.presentation.home.HomeUiState
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.add_transaction
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    component: HomeComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    when (val state = component.uiState.collectAsState().value) {
        is HomeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is HomeUiState.Success -> HomeScreenContent(contentPadding, state, component)
    }
}

@Composable
private fun HomeScreenContent(
    contentPadding: PaddingValues,
    state: HomeUiState.Success,
    component: HomeComponent,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding() + 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item(key = "header") { HomeTopBar() }
            item(key = "total_balance") {
                TotalBalanceCard(
                    totalBalance = state.totalBalance,
                    percentChange = state.percentChange,
                    isPositiveChange = state.isPositiveChange,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item(key = "income_expense") {
                IncomeExpenseRow(
                    income = state.income,
                    expenses = state.expenses,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item(key = "select_period") {
                PeriodFilterChips(
                    selectedPeriod = state.selectedPeriod,
                    onPeriodSelected = remember(component) {
                        { component.onEvent(HomeEvent.PeriodSelected(it)) }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item(key = "recent_transactions") {
                RecentTransactionsHeader(
                    onViewAllClick = remember(component) {
                        { component.onEvent(HomeEvent.ViewAllTransactionsClicked) }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            items(
                items = state.recentTransactions,
                key = { it.id },
            ) { transaction ->
                TransactionItem(
                    name = transaction.name,
                    category = "${transaction.category} \u2022 ${transaction.date}",
                    amount = transaction.amount,
                    isIncome = transaction.isIncome,
                    icon = {
                        Icon(
                            imageVector = mapCategoryIcon(transaction.iconName),
                            contentDescription = transaction.category,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        FloatingActionButton(
            onClick = { component.onEvent(HomeEvent.AddTransactionClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = contentPadding.calculateBottomPadding() + 16.dp,
                ),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.add_transaction),
            )
        }
    }
}
