package com.expense.tracker.core.domain.model

import com.expense.tracker.core.data.local.entity.TransactionType
import java.time.LocalDate

data class Expense(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val note: String? = null,
    val category: Category? = null,
    val date: LocalDate,
    val type: TransactionType,
    val createdAt: Long = System.currentTimeMillis(),
)
