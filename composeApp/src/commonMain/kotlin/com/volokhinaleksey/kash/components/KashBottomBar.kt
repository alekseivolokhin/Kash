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
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
        )

        NavigationBarItem(
            selected = activeChild is RootChild.Transactions,
            onClick = onTransactionsClick,
            icon = { Icon(Icons.Default.Receipt, contentDescription = "Transactions") },
            label = { Text("Transactions") },
        )

        NavigationBarItem(
            selected = activeChild is RootChild.Stats,
            onClick = onStatsClick,
            icon = { Icon(Icons.Default.Analytics, contentDescription = "Stats") },
            label = { Text("Stats") },
        )

        NavigationBarItem(
            selected = activeChild is RootChild.Settings,
            onClick = onSettingsClick,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
        )
    }
}