package com.volokhinaleksey.kash.designsystem.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.KashDimens
import com.volokhinaleksey.kash.designsystem.button.KashIconButton
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.app_name
import kash.composeapp.generated.resources.back
import kash.composeapp.generated.resources.kash_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun KashTopBar(
    modifier: Modifier = Modifier,
    leading: @Composable (() -> Unit)? = null,
    title: String? = null,
    largeTitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = KashDimens.ScreenHorizontalPadding)
            .padding(top = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                if (leading != null) leading()
                if (title != null) {
                    Text(
                        text = title,
                        color = Kash.colors.text,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp,
                    )
                }
            }
            if (trailing != null) {
                Box(contentAlignment = Alignment.Center) { trailing() }
            } else {
                Spacer(Modifier.size(KashDimens.IconButtonSize))
            }
        }
        if (largeTitle != null) {
            Text(
                text = largeTitle,
                modifier = Modifier.padding(top = 14.dp),
                color = Kash.colors.text,
                fontSize = 32.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1.2).sp,
            )
        }
    }
}

@Composable
fun KashLogoTopBar(
    modifier: Modifier = Modifier,
    largeTitle: String? = null,
    onNotificationsClick: () -> Unit = {},
    showNotificationDot: Boolean = true,
    showNotifications: Boolean = true,
) {
    KashTopBar(
        modifier = modifier,
        leading = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Image(
                    painter = painterResource(Res.drawable.kash_logo),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                )
                if (largeTitle == null) {
                    Text(
                        text = stringResource(Res.string.app_name),
                        color = Kash.colors.text,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp,
                    )
                }
            }
        },
        largeTitle = largeTitle,
        trailing = if (showNotifications) {
            { KashNotificationButton(onClick = onNotificationsClick, showDot = showNotificationDot) }
        } else {
            null
        },
    )
}

@Composable
fun KashSectionTopBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    KashTopBar(
        modifier = modifier,
        title = title,
    )
}

@Composable
fun KashBackTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    showNotifications: Boolean = false,
    onNotificationsClick: () -> Unit = {},
) {
    KashTopBar(
        modifier = modifier,
        leading = {
            KashIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(Res.string.back),
                onClick = onBackClick,
            )
        },
        title = title.takeIf { it.isNotEmpty() },
        trailing = if (showNotifications) {
            { KashNotificationButton(onClick = onNotificationsClick) }
        } else {
            null
        },
    )
}
