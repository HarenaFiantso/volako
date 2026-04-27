package com.expense.tracker.core.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.dao.ExpenseDao
import com.expense.tracker.core.data.local.entity.TransactionType
import com.expense.tracker.core.domain.model.Expense
import com.expense.tracker.core.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl
    @Inject
    constructor(
        private val expenseDao: ExpenseDao,
        private val categoryDao: CategoryDao,
    ) : ExpenseRepository {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun observeAll(): Flow<List<Expense>> =
            combine(
                expenseDao.observeAll(),
                categoryDao.observeAll(),
            ) { expenses, categories ->
                val categoryMap = categories.associateBy { it.id }
                expenses.map { it.toDomain(categoryMap[it.categoryId]?.toDomain()) }
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun observeById(id: Long): Flow<Expense?> =
            expenseDao.observeById(id).map { entity ->
                entity?.let {
                    val category = it.categoryId?.let { catId -> categoryDao.getById(catId)?.toDomain() }
                    it.toDomain(category)
                }
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun observeByDateRange(
            startDate: Long,
            endDate: Long,
        ): Flow<List<Expense>> =
            combine(
                expenseDao.observeByDateRange(startDate, endDate),
                categoryDao.observeAll(),
            ) { expenses, categories ->
                val categoryMap = categories.associateBy { it.id }
                expenses.map { it.toDomain(categoryMap[it.categoryId]?.toDomain()) }
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun observeByType(type: TransactionType): Flow<List<Expense>> =
            combine(
                expenseDao.observeByType(type),
                categoryDao.observeAll(),
            ) { expenses, categories ->
                val categoryMap = categories.associateBy { it.id }
                expenses.map { it.toDomain(categoryMap[it.categoryId]?.toDomain()) }
            }

        override fun observeTotalByTypeAndDateRange(
            type: TransactionType,
            startDate: Long,
            endDate: Long,
        ): Flow<Double> = expenseDao.observeTotalByTypeAndDateRange(type, startDate, endDate)

        @RequiresApi(Build.VERSION_CODES.O)
        override fun search(query: String): Flow<List<Expense>> =
            combine(
                expenseDao.search(query),
                categoryDao.observeAll(),
            ) { expenses, categories ->
                val categoryMap = categories.associateBy { it.id }
                expenses.map { it.toDomain(categoryMap[it.categoryId]?.toDomain()) }
            }

        @RequiresApi(Build.VERSION_CODES.O)
        override suspend fun getById(id: Long): Expense? {
            val entity = expenseDao.getById(id)
            return entity?.let {
                val category = it.categoryId?.let { catId -> categoryDao.getById(catId)?.toDomain() }
                it.toDomain(category)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override suspend fun save(expense: Expense): Long = expenseDao.insert(expense.toEntity())

        @RequiresApi(Build.VERSION_CODES.O)
        override suspend fun update(expense: Expense) {
            expenseDao.update(expense.toEntity())
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override suspend fun delete(expense: Expense) {
            expenseDao.delete(expense.toEntity())
        }

        override suspend fun deleteById(id: Long) {
            expenseDao.deleteById(id)
        }

        override suspend fun getTotalExpensesByCategoryAndDateRange(
            categoryId: Long,
            startDate: Long,
            endDate: Long,
        ): Double = expenseDao.getTotalExpensesByCategoryAndDateRange(categoryId, startDate, endDate)
    }
