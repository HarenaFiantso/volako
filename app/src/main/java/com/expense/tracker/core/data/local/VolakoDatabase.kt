package com.expense.tracker.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.dao.ExpenseDao
import com.expense.tracker.core.data.local.entity.CategoryEntity
import com.expense.tracker.core.data.local.entity.ExpenseEntity

@Database(
    entities = [
        ExpenseEntity::class,
        CategoryEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class VolakoDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    abstract fun categoryDao(): CategoryDao
}
