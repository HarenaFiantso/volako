package com.expense.tracker.core.domain.model

data class Budget(
    val id: Long = 0,
    val category: Category? = null,
    val amountLimit: Double,
    val month: Int,
    val year: Int,
    val amountSpent: Double = 0.0,
) {
    val remainingAmount: Double get() = amountLimit - amountSpent
    val progressPercent: Float get() = (amountSpent / amountLimit).coerceIn(0.0, 1.0).toFloat()
    val isOverBudget: Boolean get() = amountSpent > amountLimit
}
