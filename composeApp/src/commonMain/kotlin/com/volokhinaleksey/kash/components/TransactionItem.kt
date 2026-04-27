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
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun TransactionItem(
    name: String,
    category: String,
    amount: String,
    isIncome: Boolean,
    iconName: String,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    bankId: String? = null,
) {
    val swatch = categorySwatchFor(iconName)
    val bank = bankId?.let { id -> Bank.entries.firstOrNull { it.id == id } }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(modifier = Modifier.size(42.dp), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(swatch.bg),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = mapCategoryIcon(iconName),
                        contentDescription = null,
                        tint = swatch.fg,
                        modifier = Modifier.size(19.dp),
                    )
                }
                if (bank != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 3.dp, y = 3.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Kash.colors.bg)
                            .padding(1.5.dp),
                    ) {
                        BankBadge(bank = bank, size = 14.dp, cornerRadius = 4.dp)
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        letterSpacing = (-0.2).sp,
                    ),
                    color = Kash.colors.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 12.5.sp,
                        letterSpacing = 0.sp,
                    ),
                    color = Kash.colors.sub,
                )
            }

            AmountText(
                amount = amount,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    letterSpacing = (-0.3).sp,
                ),
                color = if (isIncome) Kash.colors.pos else Kash.colors.text,
                currencyColor = Kash.colors.fade,
                currencyWeight = FontWeight.Normal,
                currencyScale = 0.8f,
            )
        }
        if (showDivider) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Kash.colors.line),
            )
        }
    }
}
