package com.volokhinaleksey.kash.di

import app.cash.sqldelight.db.SqlDriver
import com.volokhinaleksey.kash.data.DatabaseSeeder
import com.volokhinaleksey.kash.data.db.KashDatabase
import com.volokhinaleksey.kash.data.repository.CategoryRepositoryImpl
import com.volokhinaleksey.kash.data.repository.TransactionRepositoryImpl
import com.volokhinaleksey.kash.domain.repository.CategoryRepository
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import com.volokhinaleksey.kash.domain.usecase.GetBalanceSummaryUseCase
import com.volokhinaleksey.kash.domain.usecase.GetRecentTransactionsUseCase
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single { KashDatabase(get<SqlDriver>()) }
    single { get<KashDatabase>().kashDatabaseQueries }
    single { DatabaseSeeder(get()).also { it.seedIfEmpty() } }

    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }

    factory { GetBalanceSummaryUseCase(get()) }
    factory { GetRecentTransactionsUseCase(get(), get()) }
}

fun initKoin(platformConfig: KoinApplication.() -> Unit = {}) {
    startKoin {
        platformConfig()
        modules(platformModule, appModule)
    }
}
