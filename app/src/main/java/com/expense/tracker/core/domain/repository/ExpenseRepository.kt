package com.expense.tracker.core.domain.repository

import com.expense.tracker.core.data.local.entity.TransactionType
import com.expense.tracker.core.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun observeAll(): Flow<List<Expense>>

    fun observeById(id: Long): Flow<Expense?>

    fun observeByDateRange(
        startDate: Long,
        endDate: Long,
    ): Flow<List<Expense>>

    fun observeByType(type: TransactionType): Flow<List<Expense>>

    fun observeTotalByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double>

    fun search(query: String): Flow<List<Expense>>

    suspend fun getById(id: Long): Expense?

    suspend fun save(expense: Expense): Long

    suspend fun update(expense: Expense)

    suspend fun delete(expense: Expense)

    suspend fun deleteById(id: Long)

    suspend fun getTotalExpensesByCategoryAndDateRange(
        categoryId: Long,
        startDate: Long,
        endDate: Long,
    ): Double
}
