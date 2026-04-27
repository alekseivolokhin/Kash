package com.volokhinaleksey.kash.navigation.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.EmptyHomeContent
import com.volokhinaleksey.kash.components.IncomeExpenseRow
import com.volokhinaleksey.kash.components.PeriodFilterChips
import com.volokhinaleksey.kash.components.RecentTransactionsHeader
import com.volokhinaleksey.kash.components.TotalBalanceCard
import com.volokhinaleksey.kash.components.TransactionItem
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.bank.AccountTypeBadge
import com.volokhinaleksey.kash.designsystem.bank.AccountTypeStyle
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.designsystem.bank.BankBadge
import com.volokhinaleksey.kash.designsystem.topbar.KashLogoTopBar
import com.volokhinaleksey.kash.domain.model.Period
import com.volokhinaleksey.kash.presentation.home.HomeAccountChip
import com.volokhinaleksey.kash.presentation.home.HomeCurrencyChip
import com.volokhinaleksey.kash.presentation.home.HomeEvent
import com.volokhinaleksey.kash.presentation.home.HomeUiState
import com.volokhinaleksey.kash.presentation.home.TransactionUiModel
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.accounts_strip_all
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
                Column(modifier = Modifier.padding(horizontal = KashDimens.ScreenHorizontalPadding)) {
                    TotalBalanceCard(
                        totalBalance = state.totalBalance,
                        percentChange = state.percentChange,
                        isPositiveChange = state.isPositiveChange,
                    )
                    if (state.currencyChips.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            state.currencyChips.forEach { chip ->
                                HomeCurrencyChipView(chip)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
            if (state.accountsStrip.isNotEmpty()) {
                item(key = "accounts_strip") {
                    HomeAccountsStrip(
                        accounts = state.accountsStrip,
                        onAccountClick = { id -> onEvent(HomeEvent.AccountSelected(id)) },
                        onAllAccountsClick = { onEvent(HomeEvent.AllAccountsClicked) },
                    )
                    Spacer(Modifier.height(16.dp))
                }
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
                    bankId = transaction.bankId,
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

@Composable
private fun HomeAccountsStrip(
    accounts: List<HomeAccountChip>,
    onAccountClick: (String) -> Unit,
    onAllAccountsClick: () -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = KashDimens.ScreenHorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(accounts, key = { it.id }) { acc ->
            HomeAccountTile(
                account = acc,
                onClick = { onAccountClick(acc.id) },
            )
        }
        item(key = "all_accounts") {
            HomeAllAccountsTile(onClick = onAllAccountsClick)
        }
    }
}

@Composable
private fun HomeAccountTile(
    account: HomeAccountChip,
    onClick: () -> Unit,
) {
    val bank = Bank.entries.firstOrNull { it.id == account.bankId } ?: Bank.Cash
    val borderColor = if (account.selected) Kash.colors.accent else Kash.colors.line
    val typeStyle = when (account.typeShort) {
        "CASH" -> AccountTypeStyle.Cash
        "DEBIT" -> AccountTypeStyle.Debit
        "CREDIT" -> AccountTypeStyle.Credit
        "DEPOSIT" -> AccountTypeStyle.Deposit
        else -> AccountTypeStyle.Debit
    }
    Column(
        modifier = Modifier
            .width(168.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.card)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BankBadge(bank = bank, size = 26.dp, cornerRadius = 7.dp)
            AccountTypeBadge(type = typeStyle)
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = account.name,
            color = Kash.colors.sub,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = account.balance,
                color = Kash.colors.text,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.5).sp,
            )
            Spacer(Modifier.width(3.dp))
            Text(
                text = account.currencySymbol,
                color = Kash.colors.fade,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun HomeAllAccountsTile(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Kash.colors.lineStrong, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.height(20.dp))
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Kash.colors.sub,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(Res.string.accounts_strip_all),
            color = Kash.colors.sub,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun HomeCurrencyChipView(chip: HomeCurrencyChip) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(if (Kash.colors.isDark) Kash.colors.cardAlt else Kash.colors.chipBg)
            .padding(horizontal = 9.dp, vertical = 4.dp),
    ) {
        Text(
            text = "${chip.code} ${chip.amount}",
            color = Kash.colors.sub,
            style = TextStyle(
                fontFamily = JetBrainsMonoFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                letterSpacing = 0.2.sp,
            ),
        )
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
