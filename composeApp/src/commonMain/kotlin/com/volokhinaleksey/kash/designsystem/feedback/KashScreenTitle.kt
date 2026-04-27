package com.volokhinaleksey.kash.designsystem.feedback

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.InterTightFontFamily
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun KashScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = Kash.colors.text,
        fontFamily = InterTightFontFamily(),
        fontSize = 30.sp,
        lineHeight = 33.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-1.2).sp,
        modifier = modifier,
    )
}

@Composable
fun KashScreenSubtitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = Kash.colors.sub,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        modifier = modifier,
    )
}
