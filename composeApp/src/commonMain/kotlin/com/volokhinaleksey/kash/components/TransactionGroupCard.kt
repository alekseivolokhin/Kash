package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
                TransactionGroupRow(item = item)
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
private fun TransactionGroupLabel.asText(): String = when (this) {
    TransactionGroupLabel.Today -> stringResource(Res.string.transactions_group_today)
    TransactionGroupLabel.Yesterday -> stringResource(Res.string.transactions_group_yesterday)
    is TransactionGroupLabel.Date -> text
}
