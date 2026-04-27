package com.volokhinaleksey.kash.presentation.accounts

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.volokhinaleksey.kash.designsystem.bank.Bank

@Immutable
data class AddAccountUiState(
    val type: AccountType,
    val initialBalance: String,
    val name: String,
    val bank: Bank,
    val currencyCode: String,
    val currencyDisplay: String,
    val colorPalette: List<Color>,
    val selectedColorIndex: Int,
)
