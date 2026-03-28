package com.volokhinaleksey.kash.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.volokhinaleksey.kash.navigation.home.HomeComponent
import com.volokhinaleksey.kash.navigation.settings.SettingsComponent
import com.volokhinaleksey.kash.navigation.stats.StatsComponent
import com.volokhinaleksey.kash.navigation.transactions.TransactionsComponent
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<RootConfig>()

    val childStack: Value<ChildStack<RootConfig, RootChild>> = childStack(
        source = navigation,
        serializer = RootConfig.serializer(),
        initialConfiguration = RootConfig.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: RootConfig,
        componentContext: ComponentContext,
    ): RootChild = when (config) {
        is RootConfig.Home -> RootChild.Home(HomeComponent(componentContext))
        is RootConfig.Transactions -> RootChild.Transactions(TransactionsComponent(componentContext))
        is RootConfig.Stats -> RootChild.Stats(StatsComponent(componentContext))
        is RootConfig.Settings -> RootChild.Settings(SettingsComponent(componentContext))
    }

    fun onHomeTabClicked() {
        navigation.bringToFront(RootConfig.Home)
    }

    fun onTransactionsTabClicked() {
        navigation.bringToFront(RootConfig.Transactions)
    }

    fun onStatsTabClicked() {
        navigation.bringToFront(RootConfig.Stats)
    }

    fun onSettingsTabClicked() {
        navigation.bringToFront(RootConfig.Settings)
    }
}

@Serializable
sealed interface RootConfig {
    @Serializable
    data object Home : RootConfig
    @Serializable
    data object Transactions : RootConfig
    @Serializable
    data object Stats : RootConfig
    @Serializable
    data object Settings : RootConfig
}

sealed class RootChild {
    data class Home(val component: HomeComponent) : RootChild()
    data class Transactions(val component: TransactionsComponent) : RootChild()
    data class Stats(val component: StatsComponent) : RootChild()
    data class Settings(val component: SettingsComponent) : RootChild()
}