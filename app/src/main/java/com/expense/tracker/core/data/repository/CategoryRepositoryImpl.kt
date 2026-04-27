package com.expense.tracker.core.data.repository

import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.domain.model.Category
import com.expense.tracker.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl
    @Inject
    constructor(
        private val categoryDao: CategoryDao,
    ) : CategoryRepository {
        override fun observeAll(): Flow<List<Category>> =
            categoryDao.observeAll().map { entities ->
                entities.map { it.toDomain() }
            }

        override suspend fun getById(id: Long): Category? = categoryDao.getById(id)?.toDomain()

        override suspend fun save(category: Category): Long = categoryDao.insert(category.toEntity())

        override suspend fun update(category: Category) {
            categoryDao.update(category.toEntity())
        }

        override suspend fun delete(category: Category) {
            categoryDao.delete(category.toEntity())
        }

        override suspend fun seedDefaultCategories() {
            // Implementation for seeding default categories if needed
        }
    }
