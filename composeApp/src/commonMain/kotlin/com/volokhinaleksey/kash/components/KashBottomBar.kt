package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.navigation.RootChild
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.nav_home
import kash.composeapp.generated.resources.nav_settings
import kash.composeapp.generated.resources.nav_stats
import kash.composeapp.generated.resources.nav_transactions
import org.jetbrains.compose.resources.stringResource

@Composable
fun KashBottomBar(
    activeChild: RootChild,
    onHomeClick: () -> Unit,
    onTransactionsClick: () -> Unit,
    onStatsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Kash.colors.bg)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BottomBarTab(
            icon = Icons.Outlined.Home,
            label = stringResource(Res.string.nav_home),
            selected = activeChild is RootChild.Home,
            onClick = onHomeClick,
        )
        BottomBarTab(
            icon = Icons.AutoMirrored.Outlined.FormatListBulleted,
            label = stringResource(Res.string.nav_transactions),
            selected = activeChild is RootChild.Transactions,
            onClick = onTransactionsClick,
        )
        BottomBarTab(
            icon = Icons.Outlined.BarChart,
            label = stringResource(Res.string.nav_stats),
            selected = activeChild is RootChild.Stats,
            onClick = onStatsClick,
        )
        BottomBarTab(
            icon = Icons.Outlined.Settings,
            label = stringResource(Res.string.nav_settings),
            selected = activeChild is RootChild.Settings,
            onClick = onSettingsClick,
        )
    }
}

@Composable
private fun BottomBarTab(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val labelColor = if (selected) Kash.colors.text else Kash.colors.sub
    val iconTint = if (selected) Kash.colors.text else Kash.colors.sub
    val pillBg = if (selected) Kash.colors.card else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 10.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(pillBg)
                .padding(horizontal = 18.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            ),
            color = labelColor,
        )
    }
}
