package com.volokhinaleksey.kash.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.volokhinaleksey.kash.components.KashBottomBar
import com.volokhinaleksey.kash.navigation.home.HomeScreen
import com.volokhinaleksey.kash.navigation.settings.SettingsScreen
import com.volokhinaleksey.kash.navigation.stats.StatsScreen
import com.volokhinaleksey.kash.navigation.transactions.TransactionsScreen

@Composable
fun RootContent(component: RootComponent) {
    val childStack by component.childStack.subscribeAsState()
    val activeChild = childStack.active.instance

    Scaffold(
        bottomBar = {
            KashBottomBar(
                activeChild = activeChild,
                onHomeClick = component::onHomeTabClicked,
                onTransactionsClick = component::onTransactionsTabClicked,
                onStatsClick = component::onStatsTabClicked,
                onSettingsClick = component::onSettingsTabClicked,
            )
        }
    ) { innerPadding ->
        Children(stack = childStack) { child ->
            when (val instance = child.instance) {
                is RootChild.Home -> HomeScreen(instance.component, contentPadding = innerPadding)
                is RootChild.Transactions -> TransactionsScreen(instance.component)
                is RootChild.Stats -> StatsScreen(instance.component)
                is RootChild.Settings -> SettingsScreen(instance.component)
            }
        }
    }
}