package com.volokhinaleksey.kash.data

class DatabaseSeeder(private val queries: KashDatabaseQueries) {

    fun seedIfEmpty() {
        val existing = queries.getAllCategories().executeAsList()
        if (existing.isNotEmpty()) return

        defaultCategories.forEach { (name, icon, color) ->
            queries.insertCategory(
                name = name,
                icon = icon,
                color = color,
                is_custom = 0L,
            )
        }
    }
}

private val defaultCategories = listOf(
    Triple("Food", "restaurant", 0xFFFF8A65),
    Triple("Transport", "local_gas_station", 0xFF42A5F5),
    Triple("Shopping", "shopping_bag", 0xFFEC407A),
    Triple("Electronics", "computer", 0xFF5C6BC0),
    Triple("Subscriptions", "subscriptions", 0xFFAB47BC),
    Triple("Entertainment", "theater_comedy", 0xFFFFCA28),
    Triple("Health", "medical_services", 0xFFEF5350),
    Triple("Salary", "work", 0xFF66BB6A),
    Triple("Freelance", "work", 0xFF8FB9A8),
)
