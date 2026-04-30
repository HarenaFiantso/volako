package com.expense.tracker.core.domain.usecase

import com.expense.tracker.core.domain.model.Expense
import com.expense.tracker.core.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchExpensesUseCase
    @Inject
    constructor(
        private val expenseRepository: ExpenseRepository,
    ) {
        operator fun invoke(query: String): Flow<List<Expense>> = expenseRepository.search(query.trim())
    }
