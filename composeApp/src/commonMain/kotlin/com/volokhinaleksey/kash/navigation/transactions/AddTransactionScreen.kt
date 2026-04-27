package com.volokhinaleksey.kash.navigation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.AddTransactionTopBar
import com.volokhinaleksey.kash.components.AmountInputSection
import com.volokhinaleksey.kash.components.CategoryItem
import com.volokhinaleksey.kash.components.CategoryPickerGrid
import com.volokhinaleksey.kash.components.InputRowCard
import com.volokhinaleksey.kash.components.KashDatePickerDialog
import com.volokhinaleksey.kash.components.KashPrimaryButton
import com.volokhinaleksey.kash.components.NoteRowCard
import com.volokhinaleksey.kash.components.TransactionTypeSwitcher
import com.volokhinaleksey.kash.presentation.transactions.AddTransactionEvent
import com.volokhinaleksey.kash.presentation.transactions.AddTransactionUiState
import com.volokhinaleksey.kash.presentation.util.formatDateLong
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.add_transaction
import kash.composeapp.generated.resources.amount_label
import kash.composeapp.generated.resources.category_label
import kash.composeapp.generated.resources.expense
import kash.composeapp.generated.resources.income
import kash.composeapp.generated.resources.note_placeholder
import kash.composeapp.generated.resources.save_transaction
import kash.composeapp.generated.resources.today_prefix
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddTransactionScreen(
    component: AddTransactionComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    AddTransactionContent(
        state = state,
        onEvent = { event ->
            if (event is AddTransactionEvent.DateClicked) {
                showDatePicker = true
            } else {
                component.onEvent(event)
            }
        },
        onBackClick = remember(component) { component::onBackClicked },
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
    onEvent: (AddTransactionEvent) -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
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
            AddTransactionTopBar(
                title = stringResource(Res.string.add_transaction),
                onBackClick = onBackClick,
            )

            Spacer(Modifier.height(8.dp))

            TransactionTypeSwitcher(
                selected = state.type,
                expenseLabel = stringResource(Res.string.expense),
                incomeLabel = stringResource(Res.string.income),
                onSelected = { onEvent(AddTransactionEvent.TypeChanged(it)) },
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(32.dp))

            AmountInputSection(
                label = stringResource(Res.string.amount_label),
                amount = state.amount,
                currencySymbol = "₸",
                onAmountChange = { onEvent(AddTransactionEvent.AmountChanged(it)) },
            )

            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.category_label),
                    style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.5.sp),
                    color = Kash.colors.sub,
                )

                CategoryPickerGrid(
                    categories = state.categories.map {
                        CategoryItem(id = it.id, name = it.name, iconName = it.iconName)
                    },
                    selectedId = state.selectedCategoryId,
                    onSelected = { onEvent(AddTransactionEvent.CategorySelected(it)) },
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
                    onClick = { onEvent(AddTransactionEvent.DateClicked) },
                )
                NoteRowCard(
                    icon = Icons.AutoMirrored.Filled.Notes,
                    value = state.note,
                    placeholder = stringResource(Res.string.note_placeholder),
                    onValueChange = { onEvent(AddTransactionEvent.NoteChanged(it)) },
                )
            }

            Spacer(Modifier.height(24.dp))

            KashPrimaryButton(
                text = stringResource(Res.string.save_transaction),
                onClick = { onEvent(AddTransactionEvent.SaveClicked) },
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(16.dp + contentPadding.calculateBottomPadding()))
        }
    }
}
