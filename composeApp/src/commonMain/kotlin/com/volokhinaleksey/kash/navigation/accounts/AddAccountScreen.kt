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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.LocalAtm
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.designsystem.bank.BankBadge
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.accounts.AccountType
import com.volokhinaleksey.kash.presentation.accounts.AddAccountEvent
import com.volokhinaleksey.kash.presentation.accounts.AddAccountUiState
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.add_account_action_create
import kash.composeapp.generated.resources.add_account_field_currency
import kash.composeapp.generated.resources.add_account_field_institution
import kash.composeapp.generated.resources.add_account_field_name
import kash.composeapp.generated.resources.add_account_initial_balance
import kash.composeapp.generated.resources.add_account_section_color
import kash.composeapp.generated.resources.add_account_section_type
import kash.composeapp.generated.resources.add_account_title
import kash.composeapp.generated.resources.add_account_type_cash
import kash.composeapp.generated.resources.add_account_type_credit
import kash.composeapp.generated.resources.add_account_type_debit
import kash.composeapp.generated.resources.add_account_type_deposit
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddAccountScreen(
    component: AddAccountComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    AddAccountContent(
        state = state,
        onBackClick = { component.onEvent(AddAccountEvent.BackClicked) },
        onTypeSelected = { component.onEvent(AddAccountEvent.TypeSelected(it)) },
        onInstitutionClick = { component.onEvent(AddAccountEvent.InstitutionClicked) },
        onCurrencyClick = { component.onEvent(AddAccountEvent.CurrencyClicked) },
        onColorSelected = { component.onEvent(AddAccountEvent.ColorSelected(it)) },
        onCreateClick = { component.onEvent(AddAccountEvent.CreateClicked) },
        contentPadding = contentPadding,
    )
}

@Composable
private fun AddAccountContent(
    state: AddAccountUiState,
    onBackClick: () -> Unit,
    onTypeSelected: (AccountType) -> Unit,
    onInstitutionClick: () -> Unit,
    onCurrencyClick: () -> Unit,
    onColorSelected: (Int) -> Unit,
    onCreateClick: () -> Unit,
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
                .padding(bottom = contentPadding.calculateBottomPadding() + 100.dp),
        ) {
            KashBackTopBar(
                title = stringResource(Res.string.add_account_title),
                onBackClick = onBackClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = KashDimens.ScreenHorizontalPadding, vertical = 12.dp),
            ) {
                KashSectionLabel(text = stringResource(Res.string.add_account_section_type))
                Spacer(Modifier.height(10.dp))
                TypeRow(selected = state.type, onSelected = onTypeSelected)
                Spacer(Modifier.height(34.dp))

                BalanceDisplay(balance = state.initialBalance)
                Spacer(Modifier.height(22.dp))

                FieldRow(
                    label = stringResource(Res.string.add_account_field_name),
                    value = state.name,
                    leading = null,
                    showChevron = false,
                    onClick = null,
                )
                Spacer(Modifier.height(10.dp))
                FieldRow(
                    label = stringResource(Res.string.add_account_field_institution),
                    value = state.bank.displayName,
                    leading = { BankBadge(bank = state.bank, size = 26.dp, cornerRadius = 7.dp) },
                    showChevron = true,
                    onClick = onInstitutionClick,
                )
                Spacer(Modifier.height(10.dp))
                FieldRow(
                    label = stringResource(Res.string.add_account_field_currency),
                    value = state.currencyDisplay,
                    leading = { CurrencyTile(symbol = currencySymbolFor(state.currencyCode)) },
                    showChevron = true,
                    onClick = onCurrencyClick,
                )

                Spacer(Modifier.height(22.dp))
                KashSectionLabel(text = stringResource(Res.string.add_account_section_color))
                Spacer(Modifier.height(10.dp))
                ColorPickerRow(
                    colors = state.colorPalette,
                    selectedIndex = state.selectedColorIndex,
                    onSelected = onColorSelected,
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    start = KashDimens.ScreenHorizontalPadding,
                    end = KashDimens.ScreenHorizontalPadding,
                    bottom = contentPadding.calculateBottomPadding() + 28.dp,
                ),
        ) {
            KashButton(
                text = stringResource(Res.string.add_account_action_create),
                onClick = onCreateClick,
            )
        }
    }
}

@Composable
private fun TypeRow(
    selected: AccountType,
    onSelected: (AccountType) -> Unit,
) {
    val items = listOf(
        TypeItem(AccountType.Cash, stringResource(Res.string.add_account_type_cash), Icons.Outlined.AccountBalanceWallet),
        TypeItem(AccountType.Debit, stringResource(Res.string.add_account_type_debit), Icons.Outlined.LocalAtm),
        TypeItem(AccountType.Credit, stringResource(Res.string.add_account_type_credit), Icons.Outlined.CreditCard),
        TypeItem(AccountType.Deposit, stringResource(Res.string.add_account_type_deposit), Icons.Outlined.Savings),
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            val isSel = item.type == selected
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSel) Kash.colors.accent else Kash.colors.card)
                    .let {
                        if (!isSel) it.border(1.dp, Kash.colors.line, RoundedCornerShape(14.dp)) else it
                    }
                    .clickable { onSelected(item.type) }
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (isSel) Kash.colors.accentInk else Kash.colors.sub,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = item.label,
                    color = if (isSel) Kash.colors.accentInk else Kash.colors.text,
                    fontSize = 11.5.sp,
                    fontWeight = if (isSel) FontWeight.SemiBold else FontWeight.Medium,
                )
            }
        }
    }
}

private data class TypeItem(
    val type: AccountType,
    val label: String,
    val icon: ImageVector,
)

@Composable
private fun BalanceDisplay(balance: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        KashSectionLabel(text = stringResource(Res.string.add_account_initial_balance))
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = balance,
                color = Kash.colors.text,
                fontSize = 52.sp,
                lineHeight = 52.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = (-2.6).sp,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.size(6.dp))
            Text(
                text = "₸",
                color = Kash.colors.accent,
                fontSize = 52.sp,
                lineHeight = 52.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun FieldRow(
    label: String,
    value: String,
    leading: (@Composable () -> Unit)?,
    showChevron: Boolean,
    onClick: (() -> Unit)?,
) {
    val baseModifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(Kash.colors.card)
        .border(1.dp, Kash.colors.line, RoundedCornerShape(14.dp))
        .let { if (onClick != null) it.clickable(onClick = onClick) else it }
        .padding(horizontal = 14.dp, vertical = 12.dp)
    Row(
        modifier = baseModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (leading != null) leading()
        Column(modifier = Modifier.weight(1f)) {
            KashSectionLabel(text = label)
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                color = Kash.colors.text,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        if (showChevron) {
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Kash.colors.fade,
                modifier = Modifier.size(15.dp),
            )
        }
    }
}

@Composable
private fun CurrencyTile(symbol: String) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(Kash.colors.accentSoft),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = symbol,
            color = Kash.colors.accentSoftInk,
            style = TextStyle(
                fontFamily = JetBrainsMonoFontFamily(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

private fun currencySymbolFor(code: String): String = when (code) {
    "KZT" -> "₸"
    "USD" -> "$"
    "EUR" -> "€"
    "RUB" -> "₽"
    else -> code
}

@Composable
private fun ColorPickerRow(
    colors: List<Color>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(colors) { index, color ->
            val isSel = index == selectedIndex
            Box(
                modifier = Modifier
                    .size(if (isSel) 36.dp else 32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color)
                    .let {
                        if (isSel) it.border(2.dp, Kash.colors.accent, RoundedCornerShape(10.dp)) else it
                    }
                    .clickable { onSelected(index) },
            )
        }
    }
}

@Preview
@Composable
private fun AddAccountContentPreview() {
    KashTheme {
        AddAccountContent(
            state = AddAccountUiState(
                type = AccountType.Debit,
                initialBalance = "425 000",
                name = "Kaspi Gold",
                bank = Bank.Kaspi,
                currencyCode = "KZT",
                currencyDisplay = "KZT · Kazakhstani Tenge",
                colorPalette = listOf(
                    Color(0xFF1F3D2C),
                    Color(0xFF7A4A1E),
                    Color(0xFF2C4A66),
                    Color(0xFF5C3A66),
                    Color(0xFF7A3A30),
                    Color(0xFF5A4E2A),
                ),
                selectedColorIndex = 0,
            ),
            onBackClick = {},
            onTypeSelected = {},
            onInstitutionClick = {},
            onCurrencyClick = {},
            onColorSelected = {},
            onCreateClick = {},
        )
    }
}
