package com.volokhinaleksey.kash.designsystem.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.designsystem.KashDimens

@Composable
fun KashBottomCtaArea(
    modifier: Modifier = Modifier,
    bottomPadding: androidx.compose.ui.unit.Dp = 32.dp,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = KashDimens.ScreenHorizontalPadding)
            .padding(bottom = bottomPadding),
    ) {
        content()
    }
}
