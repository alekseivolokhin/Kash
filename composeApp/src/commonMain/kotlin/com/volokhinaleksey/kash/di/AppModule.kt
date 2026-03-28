package com.volokhinaleksey.kash.di

import com.volokhinaleksey.kash.data.repository.MockCategoryRepository
import com.volokhinaleksey.kash.data.repository.MockTransactionRepository
import com.volokhinaleksey.kash.domain.repository.CategoryRepository
import com.volokhinaleksey.kash.domain.repository.TransactionRepository
import com.volokhinaleksey.kash.domain.usecase.GetBalanceSummaryUseCase
import com.volokhinaleksey.kash.domain.usecase.GetRecentTransactionsUseCase
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single<TransactionRepository> { MockTransactionRepository() }
    single<CategoryRepository> { MockCategoryRepository() }

    factory { GetBalanceSummaryUseCase(get()) }
    factory { GetRecentTransactionsUseCase(get(), get()) }
}

fun initKoin(platformConfig: KoinApplication.() -> Unit = {}) {
    startKoin {
        platformConfig()
        modules(appModule)
    }
}
