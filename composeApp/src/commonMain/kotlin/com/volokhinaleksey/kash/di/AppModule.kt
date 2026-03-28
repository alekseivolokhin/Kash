package com.volokhinaleksey.kash.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    // Will be populated with dependencies as features are added
}

fun initKoin(platformConfig: KoinApplication.() -> Unit = {}) {
    startKoin {
        platformConfig()
        modules(appModule)
    }
}
