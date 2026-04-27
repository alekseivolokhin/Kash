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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountBalanceWallet
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.designsystem.bank.BankBadge
import com.volokhinaleksey.kash.designsystem.bank.KashAccountCardRow
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.button.KashButtonVariant
import com.volokhinaleksey.kash.designsystem.button.KashIconButton
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.topbar.KashTopBar
import com.volokhinaleksey.kash.presentation.accounts.AccountGroup
import com.volokhinaleksey.kash.presentation.accounts.AccountsEvent
import com.volokhinaleksey.kash.presentation.accounts.AccountsUiState
import com.volokhinaleksey.kash.presentation.accounts.CurrencyChip
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.back
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.accounts_empty_action_import
import kash.composeapp.generated.resources.accounts_empty_action_new
import kash.composeapp.generated.resources.accounts_empty_subtitle
import kash.composeapp.generated.resources.accounts_empty_title
import kash.composeapp.generated.resources.accounts_title
import kash.composeapp.generated.resources.accounts_total_balance
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AccountsScreen(
    component: AccountsComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    AccountsContent(
        state = state,
        onBackClick = { component.onEvent(AccountsEvent.BackClicked) },
        onAddAccountClick = { component.onEvent(AccountsEvent.AddAccountClicked) },
        onAccountClick = { id -> component.onEvent(AccountsEvent.AccountClicked(id)) },
        onImportStatementClick = { component.onEvent(AccountsEvent.ImportStatementClicked) },
        contentPadding = contentPadding,
    )
}

@Composable
private fun AccountsContent(
    state: AccountsUiState,
    onBackClick: () -> Unit,
    onAddAccountClick: () -> Unit,
    onAccountClick: (String) -> Unit,
    onImportStatementClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg)
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AccountsTopBar(
                onBackClick = onBackClick,
                onAddClick = onAddAccountClick,
            )
            if (state.isEmpty) {
                AccountsEmptyContent(
                    onAddClick = onAddAccountClick,
                    onImportClick = onImportStatementClick,
                    bottomPadding = contentPadding.calculateBottomPadding(),
                )
            } else {
                AccountsSuccessContent(
                    state = state,
                    onAccountClick = onAccountClick,
                    bottomPadding = contentPadding.calculateBottomPadding(),
                )
            }
        }
    }
}

@Composable
private fun AccountsTopBar(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
) {
    KashTopBar(
        leading = {
            KashIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(Res.string.back),
                onClick = onBackClick,
            )
        },
        title = stringResource(Res.string.accounts_title),
        trailing = {
            AddIconButton(onClick = onAddClick)
        },
    )
}

@Composable
private fun AddIconButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Kash.colors.line, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Kash.colors.text,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun AccountsSuccessContent(
    state: AccountsUiState,
    onAccountClick: (String) -> Unit,
    bottomPadding: Dp,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = KashDimens.ScreenHorizontalPadding,
            end = KashDimens.ScreenHorizontalPadding,
            top = 14.dp,
            bottom = bottomPadding + 32.dp,
        ),
    ) {
        item(key = "total") {
            TotalBlock(
                totalBalance = state.totalBalance,
                chips = state.totalCurrencyChips,
            )
            Spacer(Modifier.height(22.dp))
        }
        accountGroupItems(state.groups, onAccountClick)
    }
}

private fun LazyListScope.accountGroupItems(
    groups: List<AccountGroup>,
    onAccountClick: (String) -> Unit,
) {
    groups.forEachIndexed { index, group ->
        item(key = "group-${group.bank.id}") {
            AccountGroupBlock(
                group = group,
                onAccountClick = onAccountClick,
            )
            if (index < groups.lastIndex) Spacer(Modifier.height(22.dp))
        }
    }
}

@Composable
private fun TotalBlock(
    totalBalance: String,
    chips: List<CurrencyChip>,
) {
    Column(modifier = Modifier.padding(top = 4.dp)) {
        KashSectionLabel(text = stringResource(Res.string.accounts_total_balance))
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = totalBalance,
                color = Kash.colors.text,
                fontSize = 36.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = (-1.6).sp,
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = "₸",
                color = Kash.colors.sub,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
            )
        }
        if (chips.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                chips.forEach { chip ->
                    CurrencyChipView(chip)
                }
            }
        }
    }
}

@Composable
private fun CurrencyChipView(chip: CurrencyChip) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Kash.colors.cardAlt)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = "${chip.code} ${chip.amount}",
            color = Kash.colors.sub,
            style = TextStyle(
                fontFamily = JetBrainsMonoFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.5.sp,
                letterSpacing = 0.2.sp,
            ),
        )
    }
}

@Composable
private fun AccountGroupBlock(
    group: AccountGroup,
    onAccountClick: (String) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
        ) {
            BankBadge(bank = group.bank, size = 20.dp, cornerRadius = 6.dp)
            Text(
                text = group.bank.displayName.uppercase(),
                color = Kash.colors.sub,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Kash.colors.card)
                .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp)),
        ) {
            group.accounts.forEachIndexed { idx, account ->
                KashAccountCardRow(
                    account = account,
                    onClick = { onAccountClick(account.id) },
                )
                if (idx < group.accounts.lastIndex) {
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
}

@Composable
private fun AccountsEmptyContent(
    onAddClick: () -> Unit,
    onImportClick: () -> Unit,
    bottomPadding: Dp,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = bottomPadding + 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Kash.colors.accentSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountBalanceWallet,
                contentDescription = null,
                tint = Kash.colors.accentSoftInk,
                modifier = Modifier.size(40.dp),
            )
        }
        Spacer(Modifier.height(22.dp))
        Text(
            text = stringResource(Res.string.accounts_empty_title),
            color = Kash.colors.text,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.7).sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.accounts_empty_subtitle),
            color = Kash.colors.sub,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 280.dp).padding(horizontal = 24.dp),
        )
        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier.widthIn(max = 280.dp).fillMaxWidth().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            KashButton(
                text = stringResource(Res.string.accounts_empty_action_new),
                onClick = onAddClick,
                leadingIcon = Icons.Default.Add,
            )
            KashButton(
                text = stringResource(Res.string.accounts_empty_action_import),
                onClick = onImportClick,
                variant = KashButtonVariant.Secondary,
            )
        }
    }
}

@Preview
@Composable
private fun AccountsContentPreview() {
    KashTheme {
        val accounts = MOCK_ACCOUNTS
        AccountsContent(
            state = AccountsUiState(
                totalBalance = "1 476 700",
                totalCurrencyChips = listOf(
                    CurrencyChip("KZT", "1.05M"),
                    CurrencyChip("USD", "850"),
                    CurrencyChip("EUR", "120"),
                ),
                groups = listOf(
                    AccountGroup(Bank.Kaspi, accounts.filter { it.bank == Bank.Kaspi }),
                    AccountGroup(Bank.Halyk, accounts.filter { it.bank == Bank.Halyk }),
                ),
            ),
            onBackClick = {},
            onAddAccountClick = {},
            onAccountClick = {},
            onImportStatementClick = {},
        )
    }
}

@Preview
@Composable
private fun AccountsContentEmptyPreview() {
    KashTheme {
        AccountsContent(
            state = AccountsUiState(
                totalBalance = "0",
                totalCurrencyChips = emptyList(),
                groups = emptyList(),
            ),
            onBackClick = {},
            onAddAccountClick = {},
            onAccountClick = {},
            onImportStatementClick = {},
        )
    }
}
