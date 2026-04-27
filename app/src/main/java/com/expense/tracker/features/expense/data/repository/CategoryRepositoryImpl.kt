package com.expense.tracker.features.expense.data.repository

import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.entity.CategoryEntity
import com.expense.tracker.core.domain.model.Category
import com.expense.tracker.core.domain.repository.CategoryRepository
import com.expense.tracker.features.expense.data.mapper.toDomain
import com.expense.tracker.features.expense.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl
    @Inject
    constructor(
        private val categoryDao: CategoryDao,
    ) : CategoryRepository {
        override fun observeAll(): Flow<List<Category>> =
            categoryDao.observeAll().map { entities -> entities.map { it.toDomain() } }

        override suspend fun getById(id: Long): Category? = categoryDao.getById(id)?.toDomain()

        override suspend fun save(category: Category): Long = categoryDao.insert(category.toEntity())

        override suspend fun update(category: Category) = categoryDao.update(category.toEntity())

        override suspend fun delete(category: Category) = categoryDao.delete(category.toEntity())

        override suspend fun seedDefaultCategories() {
            if (categoryDao.count() > 0) return

            val defaults =
                listOf(
                    CategoryEntity(name = "Food & Drinks", icon = "restaurant", colorHex = "#FF6B6B", isDefault = true),
                    CategoryEntity(name = "Transport", icon = "directions_car", colorHex = "#4ECDC4", isDefault = true),
                    CategoryEntity(name = "Shopping", icon = "shopping_bag", colorHex = "#45B7D1", isDefault = true),
                    CategoryEntity(name = "Health", icon = "medical_services", colorHex = "#96CEB4", isDefault = true),
                    CategoryEntity(name = "Entertainment", icon = "movie", colorHex = "#FFEAA7", isDefault = true),
                    CategoryEntity(name = "Housing", icon = "home", colorHex = "#DDA0DD", isDefault = true),
                    CategoryEntity(name = "Education", icon = "book", colorHex = "#98D8C8", isDefault = true),
                    CategoryEntity(name = "Salary", icon = "payments", colorHex = "#A8E6CF", isDefault = true),
                    CategoryEntity(name = "Utilities", icon = "lightbulb", colorHex = "#FFD93D", isDefault = true),
                    CategoryEntity(name = "Other", icon = "inventory", colorHex = "#C7C7C7", isDefault = true),
                )
            categoryDao.insertAll(defaults)
        }
    }
