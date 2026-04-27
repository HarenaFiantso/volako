package com.expense.tracker.core.domain.usecase

import com.expense.tracker.core.domain.model.Expense
import com.expense.tracker.core.domain.repository.ExpenseRepository
import javax.inject.Inject

class SaveExpenseUseCase
    @Inject
    constructor(
        private val expenseRepository: ExpenseRepository,
    ) {
        suspend operator fun invoke(expense: Expense): Result<Long> =
            when {
                expense.title.isBlank() -> Result.failure(IllegalArgumentException("Title cannot be empty"))
                expense.amount <= 0 -> Result.failure(IllegalArgumentException("Amount must be greater than zero"))
                else ->
                    runCatching {
                        if (expense.id == 0L) {
                            expenseRepository.save(expense)
                        } else {
                            expenseRepository.update(expense)
                            expense.id
                        }
                    }
            }
    }
