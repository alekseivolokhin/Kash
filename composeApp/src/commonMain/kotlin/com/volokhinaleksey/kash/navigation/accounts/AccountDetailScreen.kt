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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.categorySwatchFor
import com.volokhinaleksey.kash.components.mapCategoryIcon
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.designsystem.bank.AccountTypeBadge
import com.volokhinaleksey.kash.designsystem.bank.currencySymbol
import com.volokhinaleksey.kash.designsystem.bank.formatBalance
import com.volokhinaleksey.kash.designsystem.bank.toBadgeStyle
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.button.KashIconButton
import com.volokhinaleksey.kash.designsystem.chip.KashSegmentItem
import com.volokhinaleksey.kash.designsystem.chip.KashSegmentedControl
import com.volokhinaleksey.kash.designsystem.topbar.KashTopBar
import com.volokhinaleksey.kash.presentation.accounts.Account
import com.volokhinaleksey.kash.presentation.accounts.AccountDetailEvent
import com.volokhinaleksey.kash.presentation.accounts.AccountDetailFilter
import com.volokhinaleksey.kash.presentation.accounts.AccountDetailTransaction
import com.volokhinaleksey.kash.presentation.accounts.AccountDetailUiState
import com.volokhinaleksey.kash.presentation.accounts.AccountType
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.accounts_balance_label
import kash.composeapp.generated.resources.accounts_empty_transfers_action
import kash.composeapp.generated.resources.accounts_empty_transfers_subtitle
import kash.composeapp.generated.resources.accounts_empty_transfers_title
import kash.composeapp.generated.resources.accounts_filter_all
import kash.composeapp.generated.resources.accounts_filter_expense
import kash.composeapp.generated.resources.accounts_filter_income
import kash.composeapp.generated.resources.accounts_filter_transfers
import kash.composeapp.generated.resources.back
import kash.composeapp.generated.resources.transactions_empty
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AccountDetailScreen(
    component: AccountDetailComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    AccountDetailContent(
        state = state,
        onBackClick = { component.onEvent(AccountDetailEvent.BackClicked) },
        onMoreClick = { component.onEvent(AccountDetailEvent.MoreClicked) },
        onFilterSelected = { component.onEvent(AccountDetailEvent.FilterSelected(it)) },
        onMakeTransferClick = { component.onEvent(AccountDetailEvent.MakeTransferClicked) },
        contentPadding = contentPadding,
    )
}

@Composable
private fun AccountDetailContent(
    state: AccountDetailUiState,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    onFilterSelected: (AccountDetailFilter) -> Unit,
    onMakeTransferClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val isTransfersEmpty = state.filter == AccountDetailFilter.Transfers
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg)
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            KashTopBar(
                leading = {
                    KashIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back),
                        onClick = onBackClick,
                    )
                },
                title = state.account.name,
                trailing = { MoreIconButton(onClick = onMoreClick) },
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = contentPadding.calculateBottomPadding() + 32.dp,
                ),
            ) {
                item(key = "hero") {
                    BankBrandHero(account = state.account)
                    Spacer(Modifier.height(16.dp))
                }
                item(key = "filter") {
                    KashSegmentedControl(
                        items = listOf(
                            KashSegmentItem(AccountDetailFilter.All, stringResource(Res.string.accounts_filter_all)),
                            KashSegmentItem(AccountDetailFilter.Income, stringResource(Res.string.accounts_filter_income)),
                            KashSegmentItem(AccountDetailFilter.Expense, stringResource(Res.string.accounts_filter_expense)),
                            KashSegmentItem(AccountDetailFilter.Transfers, stringResource(Res.string.accounts_filter_transfers)),
                        ),
                        selected = state.filter,
                        onSelected = onFilterSelected,
                        height = 44.dp,
                        modifier = Modifier.padding(horizontal = KashDimens.ScreenHorizontalPadding),
                    )
                    Spacer(Modifier.height(14.dp))
                }
                if (isTransfersEmpty) {
                    item(key = "empty_transfers") {
                        AccountDetailEmptyTransfers(
                            onMakeTransferClick = onMakeTransferClick,
                        )
                    }
                } else {
                    val visible = state.transactions.filter { matchesFilter(it, state.filter) }
                    if (visible.isEmpty()) {
                        item(key = "no_items") {
                            AccountDetailNoItems()
                        }
                    } else {
                        item(key = "tx_list") {
                            TransactionListCard(
                                items = visible,
                                modifier = Modifier.padding(horizontal = KashDimens.ScreenHorizontalPadding),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun matchesFilter(tx: AccountDetailTransaction, filter: AccountDetailFilter): Boolean = when (filter) {
    AccountDetailFilter.All -> true
    AccountDetailFilter.Income -> tx.amount > 0
    AccountDetailFilter.Expense -> tx.amount < 0
    AccountDetailFilter.Transfers -> false
}

@Composable
private fun MoreIconButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Kash.colors.line, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.MoreHoriz,
            contentDescription = null,
            tint = Kash.colors.text,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun BankBrandHero(account: Account) {
    val brand = account.bank.brandColors(Kash.colors.isDark)
    val sym = currencySymbol(account.currency)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = KashDimens.ScreenHorizontalPadding)
            .clip(RoundedCornerShape(20.dp))
            .background(brand.bg)
            .padding(horizontal = 22.dp, vertical = 22.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = account.bank.displayName,
                color = brand.fg,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.1).sp,
            )
            AccountTypeBadge(
                type = account.type.toBadgeStyle(),
                backgroundOverride = brand.fg.copy(alpha = 0.18f),
                foregroundOverride = brand.fg,
            )
        }
        Spacer(Modifier.height(26.dp))
        Text(
            text = stringResource(Res.string.accounts_balance_label).uppercase(),
            color = brand.fg.copy(alpha = 0.8f),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.2.sp,
        )
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = formatBalance(account.balance),
                color = brand.fg,
                fontSize = 38.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = (-1.8).sp,
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = sym,
                color = brand.fg.copy(alpha = 0.7f),
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
            )
        }
        if (account.lastFour != null) {
            Spacer(Modifier.height(14.dp))
            Text(
                text = "•••• ${account.lastFour}",
                color = brand.fg.copy(alpha = 0.7f),
                style = TextStyle(
                    fontFamily = JetBrainsMonoFontFamily(),
                    fontSize = 11.5.sp,
                    letterSpacing = 0.4.sp,
                ),
            )
        }
    }
}

@Composable
private fun TransactionListCard(
    items: List<AccountDetailTransaction>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp)),
    ) {
        items.forEachIndexed { idx, tx ->
            DetailTxRow(tx = tx)
            if (idx < items.lastIndex) {
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
private fun DetailTxRow(tx: AccountDetailTransaction) {
    val swatch = categorySwatchFor(tx.iconName)
    val isPositive = tx.amount > 0
    val amountColor = if (isPositive) Kash.colors.pos else Kash.colors.text
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(swatch.bg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = mapCategoryIcon(tx.iconName),
                contentDescription = null,
                tint = swatch.fg,
                modifier = Modifier.size(16.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tx.name,
                color = Kash.colors.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.2).sp,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "${tx.category} · ${tx.time}",
                color = Kash.colors.sub,
                fontSize = 12.sp,
            )
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = (if (isPositive) "+" else "−") + formatBalance(if (isPositive) tx.amount else -tx.amount),
                color = amountColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.3).sp,
            )
            Spacer(Modifier.size(3.dp))
            Text(
                text = "₸",
                color = Kash.colors.fade,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun AccountDetailNoItems() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.transactions_empty),
            color = Kash.colors.sub,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AccountDetailEmptyTransfers(
    onMakeTransferClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(if (Kash.colors.isDark) Kash.colors.cardAlt else Kash.colors.chipBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                contentDescription = null,
                tint = Kash.colors.sub,
                modifier = Modifier.size(36.dp),
            )
        }
        Spacer(Modifier.height(22.dp))
        Text(
            text = stringResource(Res.string.accounts_empty_transfers_title),
            color = Kash.colors.text,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.4).sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(Res.string.accounts_empty_transfers_subtitle),
            color = Kash.colors.sub,
            fontSize = 13.5.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 280.dp),
        )
        Spacer(Modifier.height(18.dp))
        KashButton(
            text = stringResource(Res.string.accounts_empty_transfers_action),
            onClick = onMakeTransferClick,
            modifier = Modifier.widthIn(max = 220.dp),
        )
    }
}

@Preview
@Composable
private fun AccountDetailContentPreview() {
    KashTheme {
        AccountDetailContent(
            state = AccountDetailUiState(
                account = Account(
                    id = "kaspi-gold",
                    name = "Kaspi Gold",
                    type = AccountType.Debit,
                    currency = "KZT",
                    balance = 425000,
                    bank = Bank.Kaspi,
                    lastFour = "4429",
                ),
                filter = AccountDetailFilter.All,
                transactions = listOf(
                    AccountDetailTransaction("1", "Apple Store", "Electronics", "Today · 16:20", -59900, "computer"),
                    AccountDetailTransaction("2", "NoMad Kitchen", "Food", "Today · 12:30", -12400, "restaurant"),
                    AccountDetailTransaction("3", "Salary IT LLC", "Income", "Yesterday", 145000, "work"),
                ),
            ),
            onBackClick = {},
            onMoreClick = {},
            onFilterSelected = {},
            onMakeTransferClick = {},
        )
    }
}

@Preview
@Composable
private fun AccountDetailContentEmptyTransfersPreview() {
    KashTheme {
        AccountDetailContent(
            state = AccountDetailUiState(
                account = Account(
                    id = "kaspi-gold",
                    name = "Kaspi Gold",
                    type = AccountType.Debit,
                    currency = "KZT",
                    balance = 425000,
                    bank = Bank.Kaspi,
                    lastFour = "4429",
                ),
                filter = AccountDetailFilter.Transfers,
                transactions = emptyList(),
            ),
            onBackClick = {},
            onMoreClick = {},
            onFilterSelected = {},
            onMakeTransferClick = {},
        )
    }
}
