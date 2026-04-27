package com.volokhinaleksey.kash.presentation.accounts

import androidx.compose.runtime.Immutable

enum class RateSource {
    Auto,
    Manual,
}

@Immutable
data class ExchangeRate(
    val pair: String,
    val rate: String,
    val source: RateSource,
    val updated: String,
    val stale: Boolean,
)
