package com.expense.tracker.core.domain.repository

import com.expense.tracker.core.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun observeByMonthAndYear(
        month: Int,
        year: Int,
    ): Flow<List<Budget>>

    fun observeGlobalBudget(
        month: Int,
        year: Int,
    ): Flow<Budget?>

    suspend fun save(budget: Budget): Long

    suspend fun update(budget: Budget)

    suspend fun deleteById(id: Long)
}
