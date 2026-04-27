package com.expense.tracker.core.data.local.entity

enum class TransactionType {
    EXPENSE,
    INCOME, ;

    fun toEntity(): TransactionType = TransactionType.valueOf(name)
}
