package com.expense.tracker.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.expense.tracker.core.data.local.dao.BudgetDao
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.dao.ExpenseDao
import com.expense.tracker.core.data.local.entity.BudgetEntity
import com.expense.tracker.core.data.local.entity.CategoryEntity
import com.expense.tracker.core.data.local.entity.ExpenseEntity

@Database(
    entities = [ExpenseEntity::class, CategoryEntity::class, BudgetEntity::class],
    version = 2,
    exportSchema = true,
)
@TypeConverters(VolakoTypeConverters::class)
abstract class VolakoDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    abstract fun categoryDao(): CategoryDao

    abstract fun budgetDao(): BudgetDao

    companion object {
        const val DATABASE_NAME = "volako_db"
    }
}
