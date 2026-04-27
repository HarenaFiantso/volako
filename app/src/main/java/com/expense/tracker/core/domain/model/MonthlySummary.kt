package com.expense.tracker.core.domain.model

data class MonthlySummary(
    val month: Int,
    val year: Int,
    val totalExpenses: Double,
    val totalIncome: Double,
    val balance: Double = totalIncome - totalExpenses,
    val expensesByCategory: Map<Category, Double> = emptyMap()
)
