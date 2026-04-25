package com.volokhinaleksey.kash.navigation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.components.TransactionGroupCard
import com.volokhinaleksey.kash.components.TransactionsFilterChips
import com.volokhinaleksey.kash.components.TransactionsSearchField
import com.volokhinaleksey.kash.components.TransactionsTopBar
import com.volokhinaleksey.kash.presentation.transactions.TransactionsEvent
import com.volokhinaleksey.kash.presentation.transactions.TransactionsUiState
import com.volokhinaleksey.kash.theme.Kash
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.add_transaction
import kash.composeapp.generated.resources.transactions_empty
import kash.composeapp.generated.resources.transactions_search_hint
import kash.composeapp.generated.resources.transactions_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionsScreen(
    component: TransactionsComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    TransactionsScreenContent(
        state = state,
        contentPadding = contentPadding,
        onEvent = remember(component) { component::onEvent },
    )
}

@Composable
private fun TransactionsScreenContent(
    state: TransactionsUiState,
    contentPadding: PaddingValues,
    onEvent: (TransactionsEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 8.dp,
                bottom = contentPadding.calculateBottomPadding() + 96.dp,
            ),
        ) {
            item(key = "topbar") {
                TransactionsTopBar(title = stringResource(Res.string.transactions_title))
                Spacer(Modifier.height(16.dp))
            }
            item(key = "search") {
                TransactionsSearchField(
                    value = state.searchQuery,
                    onValueChange = { onEvent(TransactionsEvent.SearchQueryChanged(it)) },
                    placeholder = stringResource(Res.string.transactions_search_hint),
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(12.dp))
            }
            item(key = "chips") {
                TransactionsFilterChips(
                    selected = state.filter,
                    onFilterSelected = { onEvent(TransactionsEvent.FilterSelected(it)) },
                    categoryFilters = state.availableCategoryFilters,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(8.dp))
            }

            when (state) {
                is TransactionsUiState.Loading -> Unit
                is TransactionsUiState.Content -> {
                    if (state.isEmpty) {
                        item(key = "empty") {
                            EmptyState(modifier = Modifier.padding(top = 64.dp))
                        }
                    } else {
                        items(
                            items = state.groups,
                            key = { it.key },
                        ) { group ->
                            TransactionGroupCard(
                                label = group.label,
                                items = group.items,
                                modifier = Modifier
                                    .padding(horizontal = 20.dp, vertical = 9.dp),
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { onEvent(TransactionsEvent.AddTransactionClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,
                    bottom = contentPadding.calculateBottomPadding() + 20.dp,
                )
                .size(52.dp),
            shape = RoundedCornerShape(16.dp),
            containerColor = Kash.colors.accent,
            contentColor = Kash.colors.accentInk,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.add_transaction),
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.transactions_empty),
            color = Kash.colors.sub,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        )
    }
}
