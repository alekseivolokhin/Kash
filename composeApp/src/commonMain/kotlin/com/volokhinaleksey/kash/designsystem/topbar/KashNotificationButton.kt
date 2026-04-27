package com.volokhinaleksey.kash.designsystem.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.notifications
import org.jetbrains.compose.resources.stringResource

@Composable
fun KashNotificationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDot: Boolean = true,
) {
    val shape = RoundedCornerShape(KashDimens.IconButtonRadius)
    Box(
        modifier = modifier
            .size(KashDimens.IconButtonSize)
            .clip(shape)
            .border(1.dp, Kash.colors.line, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = stringResource(Res.string.notifications),
            tint = Kash.colors.sub,
            modifier = Modifier.size(18.dp),
        )
        if (showDot) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 9.dp, end = 10.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Kash.colors.accent),
            )
        }
    }
}
