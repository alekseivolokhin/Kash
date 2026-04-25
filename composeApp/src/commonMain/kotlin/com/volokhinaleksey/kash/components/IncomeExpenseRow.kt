package com.volokhinaleksey.kash.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.expenses
import kash.composeapp.generated.resources.income
import org.jetbrains.compose.resources.stringResource

@Composable
fun IncomeExpenseRow(
    income: String,
    expenses: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SummaryCard(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.income),
            amount = income,
            icon = Icons.Default.ArrowDownward,
            iconBg = Kash.colors.accentSoft,
            iconFg = Kash.colors.accentSoftInk,
        )
        SummaryCard(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.expenses),
            amount = expenses,
            icon = Icons.Default.ArrowUpward,
            iconBg = Kash.colors.neg.copy(alpha = 0.16f),
            iconFg = Kash.colors.neg,
        )
    }
}

@Composable
private fun SummaryCard(
    label: String,
    amount: String,
    icon: ImageVector,
    iconBg: Color,
    iconFg: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Kash.colors.card)
            .border(
                border = BorderStroke(1.dp, Kash.colors.line),
                shape = RoundedCornerShape(18.dp),
            )
            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconFg,
                    modifier = Modifier.size(11.dp),
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.5.sp,
                    letterSpacing = 0.sp,
                ),
                color = Kash.colors.sub,
            )
        }
        Spacer(Modifier.height(12.dp))
        AmountText(
            amount = amount,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                letterSpacing = (-0.8).sp,
            ),
            color = Kash.colors.text,
            currencyColor = Kash.colors.fade,
            currencyWeight = FontWeight.Normal,
            currencyScale = 14f / 22f,
        )
    }
}
