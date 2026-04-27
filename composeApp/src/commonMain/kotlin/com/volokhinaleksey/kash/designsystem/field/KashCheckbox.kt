package com.volokhinaleksey.kash.designsystem.field

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun KashCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(22.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(if (checked) Kash.colors.accent else Color.Transparent)
            .let { base ->
                if (checked) base
                else base.border(1.5.dp, Kash.colors.lineStrong, RoundedCornerShape(7.dp))
            },
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = Kash.colors.accentInk,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

@Composable
fun KashRadio(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (selected) Kash.colors.accent else Kash.colors.lineStrong
    Box(
        modifier = modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(if (selected) Kash.colors.accent else Color.Transparent)
            .border(1.5.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(Kash.colors.accentInk),
            )
        }
    }
}
