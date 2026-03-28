package com.volokhinaleksey.kash.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.volokhinaleksey.kash.components.KashBottomBar
import com.volokhinaleksey.kash.navigation.home.HomeScreen

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
    ) {
        Children(stack = childStack) { child ->
            when (val instance = child.instance) {
                is RootChild.Home -> HomeScreen(instance.component)
                is RootChild.Transactions -> HomeScreen(instance.component)
                is RootChild.Stats -> HomeScreen(instance.component)
                is RootChild.Settings -> HomeScreen(instance.component)
            }
        }
    }
}