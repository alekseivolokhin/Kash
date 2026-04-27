package com.volokhinaleksey.kash.designsystem.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.theme.Kash

enum class KashButtonVariant { Primary, Secondary, Tertiary }

@Composable
fun KashButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: KashButtonVariant = KashButtonVariant.Primary,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.4f,
        animationSpec = tween(durationMillis = 150),
        label = "kashButtonAlpha",
    )
    val shape = RoundedCornerShape(KashDimens.ButtonRadius)
    val containerColor = when (variant) {
        KashButtonVariant.Primary -> Kash.colors.accent
        KashButtonVariant.Secondary,
        KashButtonVariant.Tertiary -> Color.Transparent
    }
    val contentColor = when (variant) {
        KashButtonVariant.Primary -> Kash.colors.accentInk
        KashButtonVariant.Secondary,
        KashButtonVariant.Tertiary -> Kash.colors.text
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha)
            .heightIn(min = KashDimens.ButtonHeight)
            .clip(shape)
            .background(containerColor)
            .let {
                if (variant == KashButtonVariant.Secondary) {
                    it.border(BorderStroke(1.dp, Kash.colors.lineStrong), shape)
                } else {
                    it
                }
            }
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.size(8.dp))
        }
        Text(
            text = text,
            color = contentColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.2).sp,
        )
    }
}
