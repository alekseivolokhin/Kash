package com.volokhinaleksey.kash.designsystem.feedback

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun KashSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
    uppercase: Boolean = true,
) {
    Text(
        text = if (uppercase) text.uppercase() else text,
        color = Kash.colors.sub,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.4.sp,
        modifier = modifier,
    )
}
