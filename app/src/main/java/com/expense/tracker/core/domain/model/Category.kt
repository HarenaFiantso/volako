package com.expense.tracker.core.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String,
    val colorHex: String,
    val isDefault: Boolean = false,
)
