package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.currency_name_aed
import kash.composeapp.generated.resources.currency_name_amd
import kash.composeapp.generated.resources.currency_name_byn
import kash.composeapp.generated.resources.currency_name_chf
import kash.composeapp.generated.resources.currency_name_cny
import kash.composeapp.generated.resources.currency_name_eur
import kash.composeapp.generated.resources.currency_name_gbp
import kash.composeapp.generated.resources.currency_name_gel
import kash.composeapp.generated.resources.currency_name_jpy
import kash.composeapp.generated.resources.currency_name_kgs
import kash.composeapp.generated.resources.currency_name_kzt
import kash.composeapp.generated.resources.currency_name_rub
import kash.composeapp.generated.resources.currency_name_try
import kash.composeapp.generated.resources.currency_name_uah
import kash.composeapp.generated.resources.currency_name_usd
import kash.composeapp.generated.resources.currency_name_uzs
import kash.composeapp.generated.resources.currency_picker_caption
import kash.composeapp.generated.resources.currency_picker_confirm
import kash.composeapp.generated.resources.currency_picker_empty
import kash.composeapp.generated.resources.currency_picker_search_hint
import kash.composeapp.generated.resources.currency_picker_section_all
import kash.composeapp.generated.resources.currency_picker_section_popular
import kash.composeapp.generated.resources.currency_picker_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class CurrencyOption(
    val code: String,
    val symbol: String,
    val nameRes: StringResource,
)

private val PopularCurrencies = listOf(
    CurrencyOption("KZT", "₸", Res.string.currency_name_kzt),
    CurrencyOption("USD", "$", Res.string.currency_name_usd),
    CurrencyOption("EUR", "€", Res.string.currency_name_eur),
    CurrencyOption("RUB", "₽", Res.string.currency_name_rub),
    CurrencyOption("GBP", "£", Res.string.currency_name_gbp),
)

private val AllCurrencies = listOf(
    CurrencyOption("AED", "د.إ", Res.string.currency_name_aed),
    CurrencyOption("AMD", "֏", Res.string.currency_name_amd),
    CurrencyOption("BYN", "Br", Res.string.currency_name_byn),
    CurrencyOption("CHF", "Fr", Res.string.currency_name_chf),
    CurrencyOption("CNY", "¥", Res.string.currency_name_cny),
    CurrencyOption("GEL", "₾", Res.string.currency_name_gel),
    CurrencyOption("JPY", "¥", Res.string.currency_name_jpy),
    CurrencyOption("KGS", "с", Res.string.currency_name_kgs),
    CurrencyOption("TRY", "₺", Res.string.currency_name_try),
    CurrencyOption("UAH", "₴", Res.string.currency_name_uah),
    CurrencyOption("UZS", "so'm", Res.string.currency_name_uzs),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerSheet(
    selectedCode: String,
    onDismiss: () -> Unit,
    onConfirm: (CurrencyOption) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    var query by rememberSaveable { mutableStateOf("") }
    var pendingCode by rememberSaveable { mutableStateOf(selectedCode) }

    val popular = remember(query) { filter(PopularCurrencies, query) }
    val all = remember(query) { filter(AllCurrencies, query) }
    val pendingOption = remember(pendingCode) {
        (PopularCurrencies + AllCurrencies).firstOrNull { it.code == pendingCode }
            ?: PopularCurrencies.first()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Kash.colors.bg,
        contentColor = Kash.colors.text,
        scrimColor = Color.Black.copy(alpha = 0.45f),
        dragHandle = { SheetDragHandle() },
    ) {
        Column(modifier = Modifier.fillMaxHeight(0.92f)) {
            SheetHeader()
            SearchBar(value = query, onValueChange = { query = it })

            Box(modifier = Modifier.weight(1f)) {
                if (popular.isEmpty() && all.isEmpty()) {
                    EmptyState(query = query)
                } else {
                    CurrencyList(
                        popular = popular,
                        all = all,
                        selectedCode = pendingCode,
                        onSelect = { pendingCode = it.code },
                    )
                }
            }

            ConfirmFooter(
                pending = pendingOption,
                onClick = { onConfirm(pendingOption) },
            )
        }
    }
}

@Composable
private fun SheetDragHandle() {
    Box(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .size(width = 36.dp, height = 4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Kash.colors.lineStrong),
    )
}

@Composable
private fun SheetHeader() {
    Text(
        text = stringResource(Res.string.currency_picker_title),
        color = Kash.colors.text,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.2).sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun SearchBar(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 4.dp, bottom = 14.dp),
    ) {
        TransactionsSearchField(
            value = value,
            onValueChange = onValueChange,
            placeholder = stringResource(Res.string.currency_picker_search_hint),
        )
    }
}

@Composable
private fun CurrencyList(
    popular: List<CurrencyOption>,
    all: List<CurrencyOption>,
    selectedCode: String,
    onSelect: (CurrencyOption) -> Unit,
) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (popular.isNotEmpty()) {
            item("popular_label") {
                SectionLabel(
                    text = stringResource(Res.string.currency_picker_section_popular),
                    paddingTop = 4.dp,
                )
            }
            item("popular_card") {
                CurrencyCard(
                    items = popular,
                    selectedCode = selectedCode,
                    onSelect = onSelect,
                )
            }
        }

        if (all.isNotEmpty()) {
            item("all_label") {
                SectionLabel(
                    text = stringResource(Res.string.currency_picker_section_all),
                    paddingTop = 20.dp,
                )
            }
            item("all_card") {
                CurrencyCard(
                    items = all,
                    selectedCode = selectedCode,
                    onSelect = onSelect,
                )
            }
        }

        item("bottom_spacer") { Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun SectionLabel(text: String, paddingTop: androidx.compose.ui.unit.Dp) {
    Text(
        text = text.uppercase(),
        color = Kash.colors.sub,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.4.sp,
        modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = paddingTop, bottom = 8.dp),
    )
}

@Composable
private fun CurrencyCard(
    items: List<CurrencyOption>,
    selectedCode: String,
    onSelect: (CurrencyOption) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Kash.colors.card)
            .border(1.dp, Kash.colors.line, RoundedCornerShape(16.dp)),
    ) {
        items.forEachIndexed { index, option ->
            CurrencyRow(
                option = option,
                selected = option.code == selectedCode,
                onClick = { onSelect(option) },
            )
            if (index != items.lastIndex) {
                HorizontalDivider(thickness = 1.dp, color = Kash.colors.line)
            }
        }
    }
}

@Composable
private fun CurrencyRow(
    option: CurrencyOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SymbolBadge(symbol = option.symbol, selected = selected)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.code,
                color = Kash.colors.text,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.2).sp,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = stringResource(option.nameRes),
                color = Kash.colors.sub,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Normal,
            )
        }
        SelectionIndicator(selected = selected)
    }
}

@Composable
private fun SymbolBadge(symbol: String, selected: Boolean) {
    val bg = if (selected) Kash.colors.accent else Kash.colors.cardAlt
    val fg = if (selected) Kash.colors.accentInk else Kash.colors.text
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = symbol,
            color = fg,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace,
        )
    }
}

@Composable
private fun SelectionIndicator(selected: Boolean) {
    if (selected) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Kash.colors.accent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = Kash.colors.accentInk,
                modifier = Modifier.size(14.dp),
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .border(1.5.dp, Kash.colors.lineStrong, CircleShape),
        )
    }
}

@Composable
private fun ConfirmFooter(pending: CurrencyOption, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Kash.colors.bg)
            .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 22.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Kash.colors.accent)
                .clickable(onClick = onClick)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(Res.string.currency_picker_confirm, pending.code),
                color = Kash.colors.accentInk,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.2).sp,
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(Res.string.currency_picker_caption),
            color = Kash.colors.fade,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EmptyState(query: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.currency_picker_empty, query),
            color = Kash.colors.sub,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
    }
}

private fun filter(items: List<CurrencyOption>, query: String): List<CurrencyOption> {
    if (query.isBlank()) return items
    val q = query.trim().lowercase()
    return items.filter { it.code.lowercase().contains(q) || it.symbol.contains(q, ignoreCase = true) }
}
