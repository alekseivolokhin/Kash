package com.volokhinaleksey.kash.navigation.transactions

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.AmountInputSection
import com.volokhinaleksey.kash.components.CategoryItem
import com.volokhinaleksey.kash.components.CategoryPickerGrid
import com.volokhinaleksey.kash.components.InputRowCard
import com.volokhinaleksey.kash.components.KashDatePickerDialog
import com.volokhinaleksey.kash.components.NoteRowCard
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.designsystem.bank.BankBadge
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.chip.KashSegmentItem
import com.volokhinaleksey.kash.designsystem.chip.KashSegmentedControl
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.domain.model.TransactionType
import com.volokhinaleksey.kash.presentation.transactions.AddTransactionEvent
import com.volokhinaleksey.kash.presentation.transactions.AddTransactionUiState
import com.volokhinaleksey.kash.presentation.transactions.AddTxTab
import com.volokhinaleksey.kash.presentation.transactions.CategoryUiModel
import com.volokhinaleksey.kash.presentation.util.formatDateLong
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.add_transaction
import kash.composeapp.generated.resources.add_tx_account_section
import kash.composeapp.generated.resources.add_tx_type_transfer
import kash.composeapp.generated.resources.amount_label
import kash.composeapp.generated.resources.category_label
import kash.composeapp.generated.resources.expense
import kash.composeapp.generated.resources.income
import kash.composeapp.generated.resources.note_placeholder
import kash.composeapp.generated.resources.rates_offline_banner_format
import kash.composeapp.generated.resources.rates_offline_banner_title
import kash.composeapp.generated.resources.save_transaction
import kash.composeapp.generated.resources.today_prefix
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddTransactionScreen(
    component: AddTransactionComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    AddTransactionContent(
        state = state,
        onTypeChange = { component.onEvent(AddTransactionEvent.TypeChanged(it)) },
        onTabSelected = { component.onEvent(AddTransactionEvent.TabSelected(it)) },
        onAmountChange = { component.onEvent(AddTransactionEvent.AmountChanged(it)) },
        onCategorySelected = { component.onEvent(AddTransactionEvent.CategorySelected(it)) },
        onNoteChange = { component.onEvent(AddTransactionEvent.NoteChanged(it)) },
        onDateClick = { showDatePicker = true },
        onAccountClick = { component.onEvent(AddTransactionEvent.AccountClicked) },
        onSaveClick = { component.onEvent(AddTransactionEvent.SaveClicked) },
        onBackClick = component::onBackClicked,
        contentPadding = contentPadding,
    )

    if (showDatePicker) {
        KashDatePickerDialog(
            initialEpochMillis = state.date,
            onDateSelected = { millis ->
                component.onEvent(AddTransactionEvent.DateChanged(millis))
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
        )
    }
}

@Composable
private fun AddTransactionContent(
    state: AddTransactionUiState,
    onTypeChange: (TransactionType) -> Unit,
    onTabSelected: (AddTxTab) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategorySelected: (Long) -> Unit,
    onNoteChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onAccountClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = contentPadding.calculateTopPadding()),
        ) {
            KashBackTopBar(
                title = stringResource(Res.string.add_transaction),
                onBackClick = onBackClick,
            )

            val staleInfo = state.staleInfo
            if (state.ratesStale && staleInfo != null) {
                Spacer(Modifier.height(4.dp))
                OfflineRatesBanner(
                    info = staleInfo,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            Spacer(Modifier.height(12.dp))

            AccountPickerRow(
                accountName = state.accountName,
                accountCurrency = state.accountCurrency,
                bankId = state.accountBankId,
                onClick = onAccountClick,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(12.dp))

            KashSegmentedControl(
                items = listOf(
                    KashSegmentItem(AddTxTab.Expense, stringResource(Res.string.expense)),
                    KashSegmentItem(AddTxTab.Income, stringResource(Res.string.income)),
                    KashSegmentItem(AddTxTab.Transfer, stringResource(Res.string.add_tx_type_transfer)),
                ),
                selected = state.tab,
                onSelected = { tab ->
                    onTabSelected(tab)
                    val type = when (tab) {
                        AddTxTab.Expense, AddTxTab.Transfer -> TransactionType.EXPENSE
                        AddTxTab.Income -> TransactionType.INCOME
                    }
                    onTypeChange(type)
                },
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(32.dp))

            AmountInputSection(
                label = stringResource(Res.string.amount_label),
                amount = state.amount,
                currencySymbol = "₸",
                onAmountChange = onAmountChange,
            )

            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                KashSectionLabel(text = stringResource(Res.string.category_label))

                CategoryPickerGrid(
                    categories = state.categories.map {
                        CategoryItem(id = it.id, name = it.name, iconName = it.iconName)
                    },
                    selectedId = state.selectedCategoryId,
                    onSelected = onCategorySelected,
                )
            }

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                InputRowCard(
                    icon = Icons.Outlined.CalendarMonth,
                    text = "${stringResource(Res.string.today_prefix)} ${formatDateLong(state.date)}",
                    onClick = onDateClick,
                )
                NoteRowCard(
                    icon = Icons.AutoMirrored.Filled.Notes,
                    value = state.note,
                    placeholder = stringResource(Res.string.note_placeholder),
                    onValueChange = onNoteChange,
                )
            }

            Spacer(Modifier.height(24.dp))

            KashButton(
                text = stringResource(Res.string.save_transaction),
                onClick = onSaveClick,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(16.dp + contentPadding.calculateBottomPadding()))
        }
    }
}

@Composable
private fun AccountPickerRow(
    accountName: String,
    accountCurrency: String,
    bankId: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bank = Bank.entries.firstOrNull { it.id == bankId } ?: Bank.Cash
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BankBadge(bank = bank, size = 28.dp, cornerRadius = 8.dp)
        Column(modifier = Modifier.weight(1f)) {
            KashSectionLabel(text = stringResource(Res.string.add_tx_account_section))
            Spacer(Modifier.height(1.dp))
            Text(
                text = "$accountName · $accountCurrency",
                color = Kash.colors.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Kash.colors.fade,
            modifier = Modifier.size(15.dp),
        )
    }
}

@Composable
private fun OfflineRatesBanner(
    info: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Kash.colors.warnSoft)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = Kash.colors.warn,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = "${stringResource(Res.string.rates_offline_banner_title)} ${stringResource(Res.string.rates_offline_banner_format, "Oct 9", info)}",
            color = Kash.colors.warn,
            fontSize = 12.5.sp,
            lineHeight = 17.5.sp,
        )
    }
}

@Preview
@Composable
private fun AddTransactionContentPreview() {
    KashTheme {
        AddTransactionContent(
            state = AddTransactionUiState(
                type = TransactionType.EXPENSE,
                amount = "5000",
                categories = listOf(
                    CategoryUiModel(1, "Food", "restaurant"),
                    CategoryUiModel(2, "Transport", "directions_car"),
                ),
                selectedCategoryId = 1,
                date = 0L,
                note = "Lunch",
            ),
            onTypeChange = {},
            onTabSelected = {},
            onAmountChange = {},
            onCategorySelected = {},
            onNoteChange = {},
            onDateClick = {},
            onAccountClick = {},
            onSaveClick = {},
            onBackClick = {},
        )
    }
}
