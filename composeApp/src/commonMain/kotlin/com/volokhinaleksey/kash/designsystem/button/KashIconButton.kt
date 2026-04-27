package com.volokhinaleksey.kash.designsystem.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.theme.Kash

enum class KashIconButtonStyle { Outlined, Filled }

@Composable
fun KashIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: KashIconButtonStyle = KashIconButtonStyle.Outlined,
    size: Dp = KashDimens.IconButtonSize,
    iconSize: Dp = 18.dp,
) {
    val shape = RoundedCornerShape(KashDimens.IconButtonRadius)
    val container = when (style) {
        KashIconButtonStyle.Outlined -> androidx.compose.ui.graphics.Color.Transparent
        KashIconButtonStyle.Filled -> Kash.colors.card
    }
    val tint = when (style) {
        KashIconButtonStyle.Outlined -> Kash.colors.sub
        KashIconButtonStyle.Filled -> Kash.colors.text
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(container)
            .let {
                if (style == KashIconButtonStyle.Outlined) {
                    it.border(1.dp, Kash.colors.line, shape)
                } else {
                    it
                }
            }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize),
        )
    }
}
