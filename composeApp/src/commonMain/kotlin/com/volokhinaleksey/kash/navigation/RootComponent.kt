package com.volokhinaleksey.kash.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.volokhinaleksey.kash.navigation.home.HomeComponent
import com.volokhinaleksey.kash.navigation.importexport.ExportComponent
import com.volokhinaleksey.kash.navigation.importexport.ImportErrorComponent
import com.volokhinaleksey.kash.navigation.importexport.ImportPickComponent
import com.volokhinaleksey.kash.navigation.importexport.ImportPreviewComponent
import com.volokhinaleksey.kash.presentation.importexport.ImportErrorUiState
import com.volokhinaleksey.kash.navigation.onboarding.OnboardingComponent
import com.volokhinaleksey.kash.navigation.settings.SettingsComponent
import com.volokhinaleksey.kash.navigation.stats.StatsComponent
import com.volokhinaleksey.kash.navigation.transactions.AddTransactionComponent
import com.volokhinaleksey.kash.navigation.transactions.TransactionsComponent
import com.volokhinaleksey.kash.presentation.settings.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<RootConfig>()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    val childStack: Value<ChildStack<RootConfig, RootChild>> = childStack(
        source = navigation,
        serializer = RootConfig.serializer(),
        initialConfiguration = RootConfig.Onboarding,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: RootConfig,
        componentContext: ComponentContext,
    ): RootChild = when (config) {
        is RootConfig.Onboarding -> RootChild.Onboarding(
            OnboardingComponent(
                componentContext = componentContext,
                onFinished = { navigation.replaceAll(RootConfig.Home) },
            )
        )

        is RootConfig.Home -> RootChild.Home(
            HomeComponent(
                componentContext = componentContext,
                onNavigateToAddTransaction = { navigation.push(RootConfig.AddTransaction) },
                onNavigateToTransactions = { navigation.bringToFront(RootConfig.Transactions) },
                onNavigateToImport = { navigation.push(RootConfig.ImportPick) },
            )
        )

        is RootConfig.Transactions -> RootChild.Transactions(
            TransactionsComponent(
                componentContext = componentContext,
                onNavigateToAddTransaction = { navigation.push(RootConfig.AddTransaction) },
            )
        )
        is RootConfig.Stats -> RootChild.Stats(
            StatsComponent(
                componentContext = componentContext,
                onAddTransaction = { navigation.push(RootConfig.AddTransaction) },
            )
        )
        is RootConfig.Settings -> RootChild.Settings(
            SettingsComponent(
                componentContext = componentContext,
                themeMode = themeMode,
                onThemeModeChange = { _themeMode.value = it },
                onImportClicked = { navigation.push(RootConfig.ImportPick) },
                onExportClicked = { navigation.push(RootConfig.Export) },
            )
        )
        is RootConfig.AddTransaction -> RootChild.AddTransaction(
            AddTransactionComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() },
            )
        )
        is RootConfig.ImportPick -> RootChild.ImportPick(
            ImportPickComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() },
                onFilePicked = { navigation.push(RootConfig.ImportPreview) },
                onFileFailed = { error ->
                    navigation.push(
                        RootConfig.ImportError(
                            fileName = error.fileName,
                            errorCode = error.errorCode,
                            pages = error.pages,
                            sizeLabel = error.sizeLabel,
                        )
                    )
                },
            )
        )
        is RootConfig.ImportError -> RootChild.ImportError(
            ImportErrorComponent(
                componentContext = componentContext,
                initialState = ImportErrorUiState(
                    fileName = config.fileName,
                    errorCode = config.errorCode,
                    pages = config.pages,
                    sizeLabel = config.sizeLabel,
                ),
                onBack = { navigation.pop() },
                onRetry = { navigation.pop() },
                onHelp = { },
            )
        )
        is RootConfig.ImportPreview -> RootChild.ImportPreview(
            ImportPreviewComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() },
            )
        )
        is RootConfig.Export -> RootChild.Export(
            ExportComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() },
            )
        )
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
    data object Onboarding : RootConfig

    @Serializable
    data object Home : RootConfig

    @Serializable
    data object Transactions : RootConfig

    @Serializable
    data object Stats : RootConfig

    @Serializable
    data object Settings : RootConfig

    @Serializable
    data object AddTransaction : RootConfig

    @Serializable
    data object ImportPick : RootConfig

    @Serializable
    data object ImportPreview : RootConfig

    @Serializable
    data object Export : RootConfig

    @Serializable
    data class ImportError(
        val fileName: String,
        val errorCode: String,
        val pages: Int,
        val sizeLabel: String,
    ) : RootConfig
}

sealed class RootChild {
    data class Onboarding(val component: OnboardingComponent) : RootChild()
    data class Home(val component: HomeComponent) : RootChild()
    data class Transactions(val component: TransactionsComponent) : RootChild()
    data class Stats(val component: StatsComponent) : RootChild()
    data class Settings(val component: SettingsComponent) : RootChild()
    data class AddTransaction(val component: AddTransactionComponent) : RootChild()
    data class ImportPick(val component: ImportPickComponent) : RootChild()
    data class ImportPreview(val component: ImportPreviewComponent) : RootChild()
    data class Export(val component: ExportComponent) : RootChild()
    data class ImportError(val component: ImportErrorComponent) : RootChild()
}