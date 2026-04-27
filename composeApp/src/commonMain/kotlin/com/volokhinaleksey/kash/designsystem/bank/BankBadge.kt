package com.volokhinaleksey.kash.designsystem.bank

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.JetBrainsMonoFontFamily
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun BankBadge(
    bank: Bank,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    cornerRadius: Dp = 9.dp,
) {
    val colors = bank.brandColors(Kash.colors.isDark)
    val initialPx = (size.value * 0.55f).sp
    val isCash = bank == Bank.Cash
    val isItalic = bank == Bank.Kaspi || bank == Bank.Jusan
    val isMono = bank == Bank.Atf || isCash

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(colors.bg),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = bank.initial,
            color = colors.fg,
            style = TextStyle(
                fontFamily = if (isMono) JetBrainsMonoFontFamily() else null,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                fontSize = if (bank == Bank.Atf) (size.value * 0.32f).sp else initialPx,
                letterSpacing = (-0.5).sp,
            ),
        )
    }
}
