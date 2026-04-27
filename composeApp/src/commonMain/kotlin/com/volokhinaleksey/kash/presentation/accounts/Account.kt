package com.volokhinaleksey.kash.presentation.accounts

import androidx.compose.runtime.Immutable
import com.volokhinaleksey.kash.designsystem.bank.Bank

enum class AccountType {
    Cash,
    Debit,
    Credit,
    Deposit,
}

@Immutable
data class Account(
    val id: String,
    val name: String,
    val type: AccountType,
    val currency: String,
    val balance: Long,
    val bank: Bank,
    val limit: Long? = null,
    val baseConv: Long? = null,
    val lastFour: String? = null,
)

@Immutable
data class AccountGroup(
    val bank: Bank,
    val accounts: List<Account>,
)
