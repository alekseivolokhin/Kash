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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.designsystem.bank.BankBadge
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.field.KashSearchField
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.accounts.InstitutionPickerEvent
import com.volokhinaleksey.kash.presentation.accounts.InstitutionPickerUiState
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.institution_picker_no_bank_subtitle
import kash.composeapp.generated.resources.institution_picker_no_bank_title
import kash.composeapp.generated.resources.institution_picker_search_hint
import kash.composeapp.generated.resources.institution_picker_section_all
import kash.composeapp.generated.resources.institution_picker_section_popular
import kash.composeapp.generated.resources.institution_picker_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InstitutionPickerScreen(
    component: InstitutionPickerComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    InstitutionPickerContent(
        state = state,
        onBackClick = { component.onEvent(InstitutionPickerEvent.BackClicked) },
        onSearchChange = { component.onEvent(InstitutionPickerEvent.SearchQueryChanged(it)) },
        onBankClick = { component.onEvent(InstitutionPickerEvent.BankSelected(it)) },
        onNoBankClick = { component.onEvent(InstitutionPickerEvent.NoBankClicked) },
        contentPadding = contentPadding,
    )
}

@Composable
private fun InstitutionPickerContent(
    state: InstitutionPickerUiState,
    onBackClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onBankClick: (Bank) -> Unit,
    onNoBankClick: () -> Unit,
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
                .padding(bottom = contentPadding.calculateBottomPadding() + 24.dp),
        ) {
            KashBackTopBar(
                title = stringResource(Res.string.institution_picker_title),
                onBackClick = onBackClick,
            )

            Spacer(Modifier.height(4.dp))
            KashSearchField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                placeholder = stringResource(Res.string.institution_picker_search_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = KashDimens.ScreenHorizontalPadding, vertical = 8.dp),
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                NoBankRow(onClick = onNoBankClick)
            }

            SectionHeader(text = stringResource(Res.string.institution_picker_section_popular))
            BankSelectionList(
                banks = state.popular,
                selectedBank = state.selectedBank,
                onBankClick = onBankClick,
            )

            SectionHeader(text = stringResource(Res.string.institution_picker_section_all))
            BankSelectionList(
                banks = state.all,
                selectedBank = state.selectedBank,
                onBankClick = onBankClick,
            )
        }
    }
}

@Composable
private fun NoBankRow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        BankBadge(bank = Bank.Cash, size = 36.dp, cornerRadius = 10.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.institution_picker_no_bank_title),
                color = Kash.colors.text,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = stringResource(Res.string.institution_picker_no_bank_subtitle),
                color = Kash.colors.sub,
                fontSize = 12.sp,
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
private fun SectionHeader(text: String) {
    KashSectionLabel(
        text = text,
        modifier = Modifier.padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 8.dp),
    )
}

@Composable
private fun BankSelectionList(
    banks: List<Bank>,
    selectedBank: Bank,
    onBankClick: (Bank) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp)),
    ) {
        banks.forEachIndexed { idx, bank ->
            BankRow(
                bank = bank,
                selected = bank == selectedBank,
                onClick = { onBankClick(bank) },
            )
            if (idx < banks.lastIndex) {
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
private fun BankRow(
    bank: Bank,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        BankBadge(bank = bank, size = 36.dp, cornerRadius = 10.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = bank.displayName,
                color = Kash.colors.text,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.2).sp,
            )
            if (bank.country != null) {
                Spacer(Modifier.height(1.dp))
                Text(
                    text = bank.country,
                    color = Kash.colors.sub,
                    fontSize = 12.sp,
                )
            }
        }
        if (selected) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(Kash.colors.accent),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Kash.colors.accentInk,
                    modifier = Modifier.size(12.dp),
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.5.dp, Kash.colors.lineStrong, RoundedCornerShape(10.dp)),
            )
        }
    }
}

@Preview
@Composable
private fun InstitutionPickerContentPreview() {
    KashTheme {
        InstitutionPickerContent(
            state = InstitutionPickerUiState(
                searchQuery = "",
                selectedBank = Bank.Kaspi,
                popular = listOf(Bank.Kaspi, Bank.Halyk, Bank.Forte, Bank.Jusan),
                all = listOf(Bank.Atf, Bank.Bereke, Bank.Revolut, Bank.Wise),
            ),
            onBackClick = {},
            onSearchChange = {},
            onBankClick = {},
            onNoBankClick = {},
        )
    }
}
