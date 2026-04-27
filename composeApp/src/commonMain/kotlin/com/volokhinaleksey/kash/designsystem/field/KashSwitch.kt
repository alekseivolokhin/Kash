package com.volokhinaleksey.kash.designsystem.field

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun KashSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark = Kash.colors.isDark
    val offTrack = if (isDark) Kash.colors.cardAlt else Kash.colors.chipBg
    val trackColor by animateColorAsState(
        targetValue = if (checked) Kash.colors.accent else offTrack,
        animationSpec = tween(durationMillis = 150),
        label = "kashSwitchTrack",
    )
    val thumbX by animateDpAsState(
        targetValue = if (checked) 18.dp else 2.dp,
        animationSpec = tween(durationMillis = 150),
        label = "kashSwitchThumbX",
    )
    val thumbColor = if (checked) Kash.colors.accentInk else Color.White
    val shape = RoundedCornerShape(999.dp)

    Box(
        modifier = modifier
            .width(38.dp)
            .height(22.dp)
            .clip(shape)
            .background(trackColor)
            .let { if (!checked) it.border(1.dp, Kash.colors.line, shape) else it }
            .clickable { onCheckedChange(!checked) },
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbX, y = 2.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(thumbColor),
        )
    }
}
