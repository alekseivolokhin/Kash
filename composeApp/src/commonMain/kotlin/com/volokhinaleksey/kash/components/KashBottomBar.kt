package com.volokhinaleksey.kash.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.navigation.RootChild
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
    NavigationBar(
        modifier = modifier,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = activeChild is RootChild.Home,
            onClick = onHomeClick,
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = stringResource(Res.string.nav_home)
                )
            },
            label = { Text(stringResource(Res.string.nav_home)) },
        )

        NavigationBarItem(
            selected = activeChild is RootChild.Transactions,
            onClick = onTransactionsClick,
            icon = {
                Icon(
                    Icons.Default.Receipt,
                    contentDescription = stringResource(Res.string.nav_transactions)
                )
            },
            label = { Text(stringResource(Res.string.nav_transactions)) },
        )

        NavigationBarItem(
            selected = activeChild is RootChild.Stats,
            onClick = onStatsClick,
            icon = {
                Icon(
                    Icons.Default.Analytics,
                    contentDescription = stringResource(Res.string.nav_stats)
                )
            },
            label = { Text(stringResource(Res.string.nav_stats)) },
        )

        NavigationBarItem(
            selected = activeChild is RootChild.Settings,
            onClick = onSettingsClick,
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(Res.string.nav_settings)
                )
            },
            label = { Text(stringResource(Res.string.nav_settings)) },
        )
    }
}