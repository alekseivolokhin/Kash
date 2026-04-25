package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.Kash
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
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.total_balance),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.4.sp,
            ),
            color = Kash.colors.sub,
        )
        Spacer(Modifier.height(10.dp))
        AmountText(
            amount = totalBalance,
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 44.sp,
                lineHeight = 44.sp,
                letterSpacing = (-2.2).sp,
            ),
            color = Kash.colors.text,
            currencyColor = Kash.colors.sub,
            currencyWeight = FontWeight.Normal,
            currencyScale = 28f / 44f,
        )
        Spacer(Modifier.height(14.dp))
        PercentChangeChip(
            text = percentChange,
            isPositive = isPositiveChange,
        )
    }
}

@Composable
private fun PercentChangeChip(
    text: String,
    isPositive: Boolean,
) {
    val bg = if (isPositive) Kash.colors.accentSoft else Kash.colors.neg.copy(alpha = 0.16f)
    val fg = if (isPositive) Kash.colors.accentSoftInk else Kash.colors.neg
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(start = 8.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
            contentDescription = null,
            tint = fg,
            modifier = Modifier.size(11.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.1).sp,
            ),
            color = fg,
        )
    }
}
