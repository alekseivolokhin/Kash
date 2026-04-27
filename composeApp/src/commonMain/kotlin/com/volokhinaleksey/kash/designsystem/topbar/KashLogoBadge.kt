package com.volokhinaleksey.kash.designsystem.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.theme.InterTightFontFamily
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun KashLogoBadge(
    letter: String,
    modifier: Modifier = Modifier,
    size: Dp = KashDimens.LogoBadgeSize,
    radius: Dp = KashDimens.LogoBadgeRadius,
    fontSize: TextUnit = 14.sp,
    letterSpacing: TextUnit = (-0.5).sp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(radius))
            .background(Kash.colors.accent),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = letter,
            color = Kash.colors.accentInk,
            fontFamily = InterTightFontFamily(),
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = letterSpacing,
        )
    }
}
