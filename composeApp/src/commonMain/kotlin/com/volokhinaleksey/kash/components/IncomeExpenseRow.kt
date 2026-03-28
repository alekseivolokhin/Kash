package com.volokhinaleksey.kash.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.theme.ExpensePink
import com.volokhinaleksey.kash.theme.IncomeGreen
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
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        KashCard(modifier = Modifier.weight(1f)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = IncomeGreen,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = stringResource(Res.string.income),
                    style = MaterialTheme.typography.labelMedium,
                    color = IncomeGreen,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = income,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        KashCard(modifier = Modifier.weight(1f)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = ExpensePink,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = stringResource(Res.string.expenses),
                    style = MaterialTheme.typography.labelMedium,
                    color = ExpensePink,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = expenses,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
