package com.volokhinaleksey.kash.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.volokhinaleksey.kash.theme.CategorySwatch
import com.volokhinaleksey.kash.theme.DarkCategoryPalette
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.LightCategoryPalette

@Composable
@ReadOnlyComposable
fun categorySwatchFor(iconName: String): CategorySwatch {
    val palette = if (Kash.colors.isDark) DarkCategoryPalette else LightCategoryPalette
    return when (iconName) {
        "restaurant" -> palette.food
        "directions_car", "local_gas_station" -> palette.transport
        "work" -> palette.income
        "shopping_bag" -> palette.shopping
        "computer" -> palette.electronics
        "subscriptions" -> palette.subscriptions
        "theater_comedy" -> palette.entertainment
        "medical_services" -> palette.shopping
        else -> palette.entertainment
    }
}
