package com.volokhinaleksey.kash.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.volokhinaleksey.kash.components.KashBottomBar
import com.volokhinaleksey.kash.navigation.home.HomeScreen
import com.volokhinaleksey.kash.navigation.importexport.ExportScreen
import com.volokhinaleksey.kash.navigation.importexport.ImportErrorScreen
import com.volokhinaleksey.kash.navigation.importexport.ImportPickScreen
import com.volokhinaleksey.kash.navigation.importexport.ImportPreviewScreen
import com.volokhinaleksey.kash.navigation.onboarding.OnboardingScreen
import com.volokhinaleksey.kash.navigation.settings.SettingsScreen
import com.volokhinaleksey.kash.navigation.stats.StatsScreen
import com.volokhinaleksey.kash.navigation.transactions.AddTransactionScreen
import com.volokhinaleksey.kash.navigation.transactions.TransactionsScreen

@Composable
fun RootContent(component: RootComponent) {
    val childStack by component.childStack.subscribeAsState()
    val activeChild = childStack.active.instance
    val showBottomBar = activeChild !is RootChild.AddTransaction &&
        activeChild !is RootChild.ImportPick &&
        activeChild !is RootChild.ImportPreview &&
        activeChild !is RootChild.ImportError &&
        activeChild !is RootChild.Export &&
        activeChild !is RootChild.Onboarding

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                KashBottomBar(
                    activeChild = activeChild,
                    onHomeClick = component::onHomeTabClicked,
                    onTransactionsClick = component::onTransactionsTabClicked,
                    onStatsClick = component::onStatsTabClicked,
                    onSettingsClick = component::onSettingsTabClicked,
                )
            }
        }
    ) { innerPadding ->
        Children(
            stack = childStack,
            animation = stackAnimation { child ->
                when (child.instance) {
                    is RootChild.AddTransaction,
                    is RootChild.ImportPick,
                    is RootChild.ImportPreview,
                    is RootChild.ImportError,
                    is RootChild.Export -> slide()
                    else -> null
                }
            },
        ) { child ->
            when (val instance = child.instance) {
                is RootChild.Onboarding -> OnboardingScreen(
                    component = instance.component,
                    contentPadding = innerPadding,
                )
                is RootChild.Home -> HomeScreen(instance.component, contentPadding = innerPadding)
                is RootChild.Transactions -> TransactionsScreen(instance.component, contentPadding = innerPadding)
                is RootChild.Stats -> StatsScreen(instance.component, contentPadding = innerPadding)
                is RootChild.Settings -> SettingsScreen(instance.component, contentPadding = innerPadding)
                is RootChild.AddTransaction -> AddTransactionScreen(
                    component = instance.component,
                    contentPadding = innerPadding,
                )
                is RootChild.ImportPick -> ImportPickScreen(
                    component = instance.component,
                    contentPadding = innerPadding,
                )
                is RootChild.ImportPreview -> ImportPreviewScreen(
                    component = instance.component,
                    contentPadding = innerPadding,
                )
                is RootChild.Export -> ExportScreen(
                    component = instance.component,
                    contentPadding = innerPadding,
                )
                is RootChild.ImportError -> ImportErrorScreen(
                    component = instance.component,
                    contentPadding = innerPadding,
                )
            }
        }
    }
}
