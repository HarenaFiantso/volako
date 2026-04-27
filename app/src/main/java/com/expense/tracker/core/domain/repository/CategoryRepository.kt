package com.expense.tracker.core.domain.repository

import com.expense.tracker.core.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeAll(): Flow<List<Category>>

    suspend fun getById(id: Long): Category?

    suspend fun save(category: Category): Long

    suspend fun update(category: Category)

    suspend fun delete(category: Category)

    suspend fun seedDefaultCategories()
}
