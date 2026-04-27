package com.expense.tracker.core.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.expense.tracker.core.data.local.entity.TransactionType
import com.expense.tracker.core.domain.model.MonthlySummary
import com.expense.tracker.core.domain.repository.CategoryRepository
import com.expense.tracker.core.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

class GetMonthlySummaryUseCase
    @Inject
    constructor(
        private val expenseRepository: ExpenseRepository,
        private val categoryRepository: CategoryRepository,
    ) {
        @RequiresApi(Build.VERSION_CODES.O)
        operator fun invoke(yearMonth: YearMonth): Flow<MonthlySummary> {
            val startDate = yearMonth.atDay(1).toEpochMilli()
            val endDate = yearMonth.atEndOfMonth().toEpochMilli()

            val expensesFlow = expenseRepository.observeByDateRange(startDate, endDate)
            val categoriesFlow = categoryRepository.observeAll()

            return combine(expensesFlow, categoriesFlow) { expenses, _ ->
                val totalExpenses = expenses.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

                val totalIncome = expenses.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }

                val expensesByCategory =
                    expenses
                        .filter { it.type == TransactionType.EXPENSE && it.category != null }
                        .groupBy { it.category!! }
                        .mapValues { (_, groupExpenses) -> groupExpenses.sumOf { it.amount } }

                MonthlySummary(
                    month = yearMonth.monthValue,
                    year = yearMonth.year,
                    totalExpenses = totalExpenses,
                    totalIncome = totalIncome,
                    expensesByCategory = expensesByCategory,
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun LocalDate.toEpochMilli(): Long = atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
