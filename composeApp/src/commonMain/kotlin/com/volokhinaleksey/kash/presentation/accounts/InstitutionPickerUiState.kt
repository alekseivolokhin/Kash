package com.volokhinaleksey.kash.presentation.accounts

import androidx.compose.runtime.Immutable
import com.volokhinaleksey.kash.designsystem.bank.Bank

@Immutable
data class InstitutionPickerUiState(
    val searchQuery: String,
    val selectedBank: Bank,
    val popular: List<Bank>,
    val all: List<Bank>,
)
