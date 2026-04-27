package com.volokhinaleksey.kash.presentation.accounts

import androidx.compose.runtime.Immutable

enum class AccountDetailFilter { All, Income, Expense, Transfers }

@Immutable
data class AccountDetailTransaction(
    val id: String,
    val name: String,
    val category: String,
    val time: String,
    val amount: Long,
    val iconName: String,
)

@Immutable
data class AccountDetailUiState(
    val account: Account,
    val filter: AccountDetailFilter,
    val transactions: List<AccountDetailTransaction>,
)
