package com.expense.tracker.features.expense.data.mapper

import com.expense.tracker.core.data.local.entity.CategoryEntity
import com.expense.tracker.core.domain.model.Category

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
