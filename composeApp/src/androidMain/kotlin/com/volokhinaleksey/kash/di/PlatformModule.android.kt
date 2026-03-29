package com.volokhinaleksey.kash.di

import com.volokhinaleksey.kash.data.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseDriverFactory(get()).createDriver() }
}
