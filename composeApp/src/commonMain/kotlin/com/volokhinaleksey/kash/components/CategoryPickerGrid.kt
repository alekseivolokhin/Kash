package com.volokhinaleksey.kash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.theme.Kash

private const val COLUMNS = 4

data class CategoryItem(
    val id: Long,
    val name: String,
    val iconName: String,
)

@Composable
fun CategoryPickerGrid(
    categories: List<CategoryItem>,
    selectedId: Long?,
    onSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        categories.chunked(COLUMNS).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { item ->
                    CategoryCell(
                        item = item,
                        isSelected = item.id == selectedId,
                        onClick = { onSelected(item.id) },
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(COLUMNS - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CategoryCell(
    item: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val swatch = categorySwatchFor(item.iconName)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val tile = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(swatch.bg)
            .let { base ->
                if (isSelected) {
                    base.border(
                        width = 1.5.dp,
                        color = Kash.colors.accent,
                        shape = RoundedCornerShape(16.dp),
                    )
                } else {
                    base
                }
            }
            .clickable(onClick = onClick)

        Box(
            modifier = tile,
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = mapCategoryIcon(item.iconName),
                contentDescription = item.name,
                tint = swatch.fg,
                modifier = Modifier.size(26.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = item.name,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) Kash.colors.text else Kash.colors.sub,
            textAlign = TextAlign.Center,
        )
    }
}
