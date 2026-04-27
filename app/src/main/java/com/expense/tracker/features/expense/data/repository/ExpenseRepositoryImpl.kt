package com.expense.tracker.features.expense.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.dao.ExpenseDao
import com.expense.tracker.core.data.local.entity.TransactionType
import com.expense.tracker.core.domain.model.Expense
import com.expense.tracker.core.domain.repository.ExpenseRepository
import com.expense.tracker.features.expense.data.mapper.toDomain
import com.expense.tracker.features.expense.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepositoryImpl
    @Inject
    constructor(
        private val expenseDao: ExpenseDao,
        private val categoryDao: CategoryDao,
    ) : ExpenseRepository {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun observeAll(): Flow<List<Expense>> =
            expenseDao.observeAll().map { entities ->
                entities.map { entity ->
                    val category = entity.categoryId?.let { categoryDao.getById(it)?.toDomain() }
                    entity.toDomain(category)
                }
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun observeById(id: Long): Flow<Expense?> =
            expenseDao.observeById(id).map { entity ->
                entity?.let {
                    val category = it.categoryId?.let { cId -> categoryDao.getById(cId)?.toDomain() }
                    it.toDomain(category)
                }
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun observeByDateRange(
            startDate: Long,
            endDate: Long,
        ): Flow<List<Expense>> =
            expenseDao.observeByDateRange(startDate, endDate).map { entities ->
                entities.map { entity ->
                    val category = entity.categoryId?.let { categoryDao.getById(it)?.toDomain() }
                    entity.toDomain(category)
                }
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun observeByType(type: TransactionType): Flow<List<Expense>> =
            expenseDao.observeByType(type.toEntity()).map { entities ->
                entities.map { entity ->
                    val category = entity.categoryId?.let { categoryDao.getById(it)?.toDomain() }
                    entity.toDomain(category)
                }
            }

        override fun observeTotalByTypeAndDateRange(
            type: TransactionType,
            startDate: Long,
            endDate: Long,
        ): Flow<Double> = expenseDao.observeTotalByTypeAndDateRange(type.toEntity(), startDate, endDate)

        @RequiresApi(Build.VERSION_CODES.O)
        override fun search(query: String): Flow<List<Expense>> =
            expenseDao.search(query).map { entities ->
                entities.map { entity ->
                    val category = entity.categoryId?.let { categoryDao.getById(it)?.toDomain() }
                    entity.toDomain(category)
                }
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override suspend fun getById(id: Long): Expense? =
            expenseDao.getById(id)?.let { entity ->
                val category = entity.categoryId?.let { categoryDao.getById(it)?.toDomain() }
                entity.toDomain(category)
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override suspend fun save(expense: Expense): Long = expenseDao.insert(expense.toEntity())

        @RequiresApi(Build.VERSION_CODES.O)
        override suspend fun update(expense: Expense) = expenseDao.update(expense.toEntity())

        @RequiresApi(Build.VERSION_CODES.O)
        override suspend fun delete(expense: Expense) = expenseDao.delete(expense.toEntity())

        override suspend fun deleteById(id: Long) = expenseDao.deleteById(id)

        override suspend fun getTotalExpensesByCategoryAndDateRange(
            categoryId: Long,
            startDate: Long,
            endDate: Long,
        ): Double = expenseDao.getTotalExpensesByCategoryAndDateRange(categoryId, startDate, endDate)
    }
