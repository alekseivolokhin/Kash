package com.volokhinaleksey.kash.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.volokhinaleksey.kash.data.CategoryEntity
import com.volokhinaleksey.kash.data.KashDatabaseQueries
import com.volokhinaleksey.kash.domain.model.Category
import com.volokhinaleksey.kash.domain.repository.CategoryRepository
import com.volokhinaleksey.kash.mappers.asDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val queries: KashDatabaseQueries,
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> = queries.getAllCategories()
        .asFlow()
        .mapToList(Dispatchers.Default)
        .map { it.map(CategoryEntity::asDomain) }

    override suspend fun getCategoryById(id: Long): Category? = queries.getCategoryById(id)
        .asFlow()
        .mapToOneOrNull(Dispatchers.Default)
        .map { it?.asDomain() }
        .firstOrNull()

    override suspend fun insertCategory(category: Category) {
        queries.insertCategory(
            name = category.name,
            icon = category.icon,
            color = category.color,
            is_custom = if (category.isCustom) 1L else 0L
        )
    }

    override suspend fun deleteCategory(id: Long) {
        queries.deleteCategory(id)
    }
}