package com.volokhinaleksey.kash.domain.repository

import com.volokhinaleksey.kash.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun insertCategory(category: Category)
    suspend fun deleteCategory(id: Long)
}
