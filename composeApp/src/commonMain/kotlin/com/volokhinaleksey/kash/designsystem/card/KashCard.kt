package com.volokhinaleksey.kash.designsystem.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.theme.Kash

@Composable
fun KashCard(
    modifier: Modifier = Modifier,
    radius: Dp = KashDimens.CardRadius,
    onClick: (() -> Unit)? = null,
    showBorder: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(radius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Kash.colors.card)
            .let { if (showBorder) it.border(1.dp, Kash.colors.line, shape) else it }
            .let { if (onClick != null) it.clickable(onClick = onClick) else it },
        content = content,
    )
}
