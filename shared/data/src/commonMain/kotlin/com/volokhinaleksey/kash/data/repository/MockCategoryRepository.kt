package com.volokhinaleksey.kash.data.repository

import com.volokhinaleksey.kash.domain.model.Category
import com.volokhinaleksey.kash.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MockCategoryRepository : CategoryRepository {

    private val mockCategories = listOf(
        Category(id = 1, name = "Electronics", icon = "computer", color = 0xFF5C6BC0),
        Category(id = 2, name = "Income", icon = "work", color = 0xFF8FB9A8),
        Category(id = 3, name = "Food", icon = "restaurant", color = 0xFFFF8A65),
        Category(id = 4, name = "Transport", icon = "local_gas_station", color = 0xFF42A5F5),
        Category(id = 5, name = "Subscriptions", icon = "subscriptions", color = 0xFFAB47BC),
    )

    private val categories = MutableStateFlow(mockCategories)

    override fun getAllCategories(): Flow<List<Category>> = categories

    override suspend fun getCategoryById(id: Long): Category? =
        categories.value.find { it.id == id }

    override suspend fun insertCategory(category: Category) {
        categories.value = categories.value + category
    }

    override suspend fun deleteCategory(id: Long) {
        categories.value = categories.value.filter { it.id != id }
    }
}
