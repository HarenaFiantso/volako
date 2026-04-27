package com.expense.tracker.core.domain.usecase

import com.expense.tracker.core.domain.repository.ExpenseRepository
import javax.inject.Inject

class DeleteExpenseUseCase
    @Inject
    constructor(
        private val expenseRepository: ExpenseRepository,
    ) {
        suspend operator fun invoke(id: Long) = expenseRepository.deleteById(id)
    }
