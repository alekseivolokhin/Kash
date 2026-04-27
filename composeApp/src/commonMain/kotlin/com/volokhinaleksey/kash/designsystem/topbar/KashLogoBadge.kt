package com.volokhinaleksey.kash.designsystem.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.kash_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun KashLogoBadge(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    Image(
        painter = painterResource(Res.drawable.kash_logo),
        contentDescription = null,
        modifier = modifier.size(size),
    )
}
