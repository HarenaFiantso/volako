package com.expense.tracker.core.data.local.database

import androidx.room.TypeConverter
import com.expense.tracker.core.data.local.entity.TransactionType

object VolakoTypeConverters {

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)
}