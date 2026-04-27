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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.components.EmptyHomeContent
import com.volokhinaleksey.kash.components.IncomeExpenseRow
import com.volokhinaleksey.kash.components.PeriodFilterChips
import com.volokhinaleksey.kash.components.RecentTransactionsHeader
import com.volokhinaleksey.kash.components.TotalBalanceCard
import com.volokhinaleksey.kash.components.TransactionItem
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.topbar.KashLogoTopBar
import com.volokhinaleksey.kash.domain.model.Period
import com.volokhinaleksey.kash.presentation.home.HomeEvent
import com.volokhinaleksey.kash.presentation.home.HomeUiState
import com.volokhinaleksey.kash.presentation.home.TransactionUiModel
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.add_transaction
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(
    component: HomeComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    HomeContent(
        state = state,
        onEvent = component::onEvent,
        contentPadding = contentPadding,
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    when (state) {
        is HomeUiState.Loading -> HomeLoadingContent()
        is HomeUiState.Empty -> HomeEmptyContent(
            contentPadding = contentPadding,
            onEvent = onEvent,
        )
        is HomeUiState.Success -> HomeSuccessContent(
            state = state,
            contentPadding = contentPadding,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun HomeLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Kash.colors.accent)
    }
}

@Composable
private fun HomeEmptyContent(
    contentPadding: PaddingValues,
    onEvent: (HomeEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg)
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        KashLogoTopBar()
        EmptyHomeContent(
            onAddTransactionClick = { onEvent(HomeEvent.AddTransactionClicked) },
            onImportStatementClick = { onEvent(HomeEvent.ImportStatementClicked) },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun HomeSuccessContent(
    state: HomeUiState.Success,
    contentPadding: PaddingValues,
    onEvent: (HomeEvent) -> Unit,
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
            item(key = "header") {
                KashLogoTopBar()
                Spacer(Modifier.height(20.dp))
            }
            item(key = "total_balance") {
                TotalBalanceCard(
                    totalBalance = state.totalBalance,
                    percentChange = state.percentChange,
                    isPositiveChange = state.isPositiveChange,
                    modifier = Modifier.padding(horizontal = KashDimens.ScreenHorizontalPadding),
                )
                Spacer(Modifier.height(16.dp))
            }
            item(key = "income_expense") {
                IncomeExpenseRow(
                    income = state.income,
                    expenses = state.expenses,
                    modifier = Modifier.padding(horizontal = KashDimens.ScreenHorizontalPadding),
                )
                Spacer(Modifier.height(20.dp))
            }
            item(key = "select_period") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = KashDimens.ScreenHorizontalPadding),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    PeriodFilterChips(
                        selectedPeriod = state.selectedPeriod,
                        onPeriodSelected = { onEvent(HomeEvent.PeriodSelected(it)) },
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
            item(key = "recent_transactions") {
                RecentTransactionsHeader(
                    onViewAllClick = { onEvent(HomeEvent.ViewAllTransactionsClicked) },
                    modifier = Modifier.padding(horizontal = KashDimens.ScreenHorizontalPadding),
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
                    modifier = Modifier.padding(horizontal = KashDimens.ScreenHorizontalPadding),
                )
            }
        }

        FloatingActionButton(
            onClick = { onEvent(HomeEvent.AddTransactionClicked) },
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
            )
        }
    }
}

@Preview
@Composable
private fun HomeContentLoadingPreview() {
    KashTheme { HomeContent(state = HomeUiState.Loading, onEvent = {}) }
}

@Preview
@Composable
private fun HomeContentEmptyPreview() {
    KashTheme { HomeContent(state = HomeUiState.Empty, onEvent = {}) }
}

@Preview
@Composable
private fun HomeContentSuccessPreview() {
    KashTheme {
        HomeContent(
            state = HomeUiState.Success(
                totalBalance = "1 248 320 ₸",
                percentChange = "+12% vs last month",
                isPositiveChange = true,
                income = "320 000 ₸",
                expenses = "184 200 ₸",
                selectedPeriod = Period.THIS_MONTH,
                recentTransactions = listOf(
                    TransactionUiModel(1, "NoMad Kitchen", "Food", "−12 400 ₸", false, "restaurant", "Today"),
                    TransactionUiModel(2, "Spotify Premium", "Subscriptions", "−2 200 ₸", false, "subscriptions", "Today"),
                    TransactionUiModel(3, "Salary", "Income", "+450 000 ₸", true, "work", "Yesterday"),
                ),
            ),
            onEvent = {},
        )
    }
}
