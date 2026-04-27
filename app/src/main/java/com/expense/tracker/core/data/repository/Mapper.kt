package com.expense.tracker.core.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.expense.tracker.core.data.local.entity.CategoryEntity
import com.expense.tracker.core.data.local.entity.ExpenseEntity
import com.expense.tracker.core.domain.model.Category
import com.expense.tracker.core.domain.model.Expense
import java.time.Instant
import java.time.ZoneId

fun CategoryEntity.toDomain(): Category =
    Category(
        id = id,
        name = name,
        icon = icon,
        colorHex = colorHex,
        isDefault = isDefault,
    )

fun Category.toEntity(): CategoryEntity =
    CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        colorHex = colorHex,
        isDefault = isDefault,
    )

@RequiresApi(Build.VERSION_CODES.O)
fun ExpenseEntity.toDomain(category: Category? = null): Expense =
    Expense(
        id = id,
        title = title,
        amount = amount,
        note = note,
        category = category,
        date = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate(),
        type = type,
        createdAt = createdAt,
    )

@RequiresApi(Build.VERSION_CODES.O)
fun Expense.toEntity(): ExpenseEntity =
    ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        note = note,
        categoryId = category?.id,
        date = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        type = type,
        createdAt = createdAt,
    )
