package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.bank.Bank
import com.volokhinaleksey.kash.designsystem.bank.BankBadge
import com.volokhinaleksey.kash.designsystem.card.KashCard
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.presentation.transactions.TransactionGroupLabel
import com.volokhinaleksey.kash.presentation.transactions.TransactionRowUiModel
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.transactions_group_today
import kash.composeapp.generated.resources.transactions_group_yesterday
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionGroupCard(
    label: TransactionGroupLabel,
    items: List<TransactionRowUiModel>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        KashSectionLabel(
            text = label.asText(),
            modifier = Modifier.padding(bottom = 6.dp),
        )

        KashCard {
            items.forEachIndexed { index, item ->
                if (item.isTransfer) {
                    TransferRow(item = item)
                } else {
                    TransactionGroupRow(item = item)
                }
                if (index < items.size - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 14.dp + 38.dp + 12.dp, end = 14.dp)
                            .height(1.dp)
                            .background(Kash.colors.line),
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionGroupRow(item: TransactionRowUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val swatch = categorySwatchFor(item.iconName)
        val bank = item.bankId?.let { id -> Bank.entries.firstOrNull { it.id == id } }
        Box(modifier = Modifier.size(38.dp), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(swatch.bg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = mapCategoryIcon(item.iconName),
                    contentDescription = null,
                    tint = swatch.fg,
                    modifier = Modifier.size(17.dp),
                )
            }
            if (bank != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 3.dp, y = 3.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Kash.colors.card)
                        .padding(1.5.dp),
                ) {
                    BankBadge(bank = bank, size = 14.dp, cornerRadius = 3.dp)
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = item.name,
                color = Kash.colors.text,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.5.sp,
                    letterSpacing = (-0.2).sp,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${item.category} · ${item.time}",
                color = Kash.colors.sub,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        AmountText(
            amount = item.amount,
            color = if (item.isIncome) Kash.colors.pos else Kash.colors.text,
            currencyColor = Kash.colors.fade,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.5.sp,
                letterSpacing = (-0.3).sp,
            ),
        )
    }
}

@Composable
private fun TransferRow(item: TransactionRowUiModel) {
    val fromBank = item.bankId?.let { id -> Bank.entries.firstOrNull { it.id == id } }
    val toBank = item.transferToBankId?.let { id -> Bank.entries.firstOrNull { it.id == id } }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(if (Kash.colors.isDark) Kash.colors.cardAlt else Kash.colors.chipBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                contentDescription = null,
                tint = Kash.colors.sub,
                modifier = Modifier.size(17.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (fromBank != null) BankBadge(bank = fromBank, size = 16.dp, cornerRadius = 4.dp)
                Text(text = "→", color = Kash.colors.sub, fontSize = 14.sp)
                if (toBank != null) BankBadge(bank = toBank, size = 16.dp, cornerRadius = 4.dp)
                Spacer(Modifier.size(2.dp))
                Text(
                    text = item.category,
                    color = Kash.colors.text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Text(
                text = "${item.name} · ${item.time}",
                color = Kash.colors.sub,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            AmountText(
                amount = item.amount,
                color = Kash.colors.text,
                currencyColor = Kash.colors.fade,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    letterSpacing = (-0.3).sp,
                ),
            )
            val convertedAmount = item.transferConvertedAmount
            if (convertedAmount != null) {
                Text(
                    text = convertedAmount,
                    color = Kash.colors.fade,
                    fontSize = 11.sp,
                )
            }
        }
    }
}

@Composable
private fun TransactionGroupLabel.asText(): String = when (this) {
    TransactionGroupLabel.Today -> stringResource(Res.string.transactions_group_today)
    TransactionGroupLabel.Yesterday -> stringResource(Res.string.transactions_group_yesterday)
    is TransactionGroupLabel.Date -> text
}
