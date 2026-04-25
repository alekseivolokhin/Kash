package com.volokhinaleksey.kash.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

fun mapCategoryIcon(iconName: String): ImageVector = when (iconName) {
    "computer" -> Icons.Default.Computer
    "work" -> Icons.Default.Work
    "restaurant" -> Icons.Default.Restaurant
    "local_gas_station" -> Icons.Default.LocalGasStation
    "subscriptions" -> Icons.Default.Subscriptions
    "shopping_bag" -> Icons.Default.ShoppingBag
    "directions_car" -> Icons.Default.DirectionsCar
    "theater_comedy" -> Icons.Default.TheaterComedy
    "medical_services" -> Icons.Default.MedicalServices
    else -> Icons.Default.ShoppingBag
}
