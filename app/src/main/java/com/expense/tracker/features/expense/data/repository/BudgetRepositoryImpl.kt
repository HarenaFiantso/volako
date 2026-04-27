package com.expense.tracker.features.expense.data.repository

import com.expense.tracker.core.data.local.dao.BudgetDao
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.entity.BudgetEntity
import com.expense.tracker.core.domain.model.Budget
import com.expense.tracker.core.domain.repository.BudgetRepository
import com.expense.tracker.features.expense.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl
    @Inject
    constructor(
        private val budgetDao: BudgetDao,
        private val categoryDao: CategoryDao,
    ) : BudgetRepository {
        override fun observeByMonthAndYear(
            month: Int,
            year: Int,
        ): Flow<List<Budget>> =
            budgetDao.observeByMonthAndYear(month, year).map { entities ->
                entities.map { entity ->
                    val category = entity.categoryId?.let { categoryDao.getById(it)?.toDomain() }
                    Budget(
                        id = entity.id,
                        category = category,
                        amountLimit = entity.amountLimit,
                        month = entity.month,
                        year = entity.year,
                    )
                }
            }

        override fun observeGlobalBudget(
            month: Int,
            year: Int,
        ): Flow<Budget?> =
            budgetDao.observeGlobalBudget(month, year).map { entity ->
                entity?.let {
                    Budget(
                        id = it.id,
                        category = null,
                        amountLimit = it.amountLimit,
                        month = it.month,
                        year = it.year,
                    )
                }
            }

        override suspend fun save(budget: Budget): Long =
            budgetDao.insert(
                BudgetEntity(
                    id = budget.id,
                    categoryId = budget.category?.id,
                    amountLimit = budget.amountLimit,
                    month = budget.month,
                    year = budget.year,
                ),
            )

        override suspend fun update(budget: Budget) =
            budgetDao.update(
                BudgetEntity(
                    id = budget.id,
                    categoryId = budget.category?.id,
                    amountLimit = budget.amountLimit,
                    month = budget.month,
                    year = budget.year,
                ),
            )

        override suspend fun deleteById(id: Long) = budgetDao.deleteById(id)
    }
