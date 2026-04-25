package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.domain.model.TransactionType
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun TransactionTypeSwitcher(
    selected: TransactionType,
    expenseLabel: String,
    incomeLabel: String,
    onSelected: (TransactionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.chipBg)
            .padding(4.dp),
    ) {
        SegmentItem(
            label = expenseLabel,
            isSelected = selected == TransactionType.EXPENSE,
            onClick = { onSelected(TransactionType.EXPENSE) },
        )
        SegmentItem(
            label = incomeLabel,
            isSelected = selected == TransactionType.INCOME,
            onClick = { onSelected(TransactionType.INCOME) },
        )
    }
}

@Composable
private fun RowScope.SegmentItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (isSelected) Kash.colors.card else Kash.colors.chipBg
    val fg = if (isSelected) Kash.colors.text else Kash.colors.sub

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            ),
            color = fg,
        )
    }
}
