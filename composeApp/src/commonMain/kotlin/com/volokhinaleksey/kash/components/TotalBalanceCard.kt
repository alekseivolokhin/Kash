package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.theme.ExpensePink
import com.volokhinaleksey.kash.theme.IncomeGreen
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.total_balance
import org.jetbrains.compose.resources.stringResource

@Composable
fun TotalBalanceCard(
    totalBalance: String,
    percentChange: String,
    isPositiveChange: Boolean,
    modifier: Modifier = Modifier,
) {
    val changeColor = if (isPositiveChange) IncomeGreen else ExpensePink
    KashCard(modifier = modifier) {
        Text(
            text = stringResource(Res.string.total_balance),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = totalBalance,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(changeColor.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = changeColor,
                modifier = Modifier.height(14.dp),
            )
            Text(
                text = percentChange,
                style = MaterialTheme.typography.labelSmall,
                color = changeColor,
            )
        }
    }
}
