package com.volokhinaleksey.kash.designsystem.bank

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash

enum class AccountTypeStyle {
    Cash,
    Debit,
    Credit,
    Deposit,
}

private val AccountTypeStyle.label: String
    get() = when (this) {
        AccountTypeStyle.Cash -> "CASH"
        AccountTypeStyle.Debit -> "DEBIT"
        AccountTypeStyle.Credit -> "CREDIT"
        AccountTypeStyle.Deposit -> "DEPOSIT"
    }

@Composable
fun AccountTypeBadge(
    type: AccountTypeStyle,
    modifier: Modifier = Modifier,
    backgroundOverride: Color? = null,
    foregroundOverride: Color? = null,
) {
    val bg = backgroundOverride ?: if (Kash.colors.isDark) Kash.colors.cardAlt else Kash.colors.chipBg
    val fg = foregroundOverride ?: Kash.colors.sub
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(bg)
            .padding(horizontal = 7.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = type.label,
            color = fg,
            style = TextStyle(
                fontFamily = JetBrainsMonoFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 9.5.sp,
                letterSpacing = 0.6.sp,
            ),
        )
    }
}
