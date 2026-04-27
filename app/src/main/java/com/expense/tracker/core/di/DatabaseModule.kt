package com.expense.tracker.core.di

import android.content.Context
import androidx.room.Room
import com.expense.tracker.core.data.local.dao.BudgetDao
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.dao.ExpenseDao
import com.expense.tracker.core.data.local.database.VolakoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): VolakoDatabase =
        Room
            .databaseBuilder(
                context,
                VolakoDatabase::class.java,
                VolakoDatabase.DATABASE_NAME,
            ).fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideExpenseDao(database: VolakoDatabase): ExpenseDao = database.expenseDao()

    @Provides
    fun provideCategoryDao(database: VolakoDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideBudgetDao(database: VolakoDatabase): BudgetDao = database.budgetDao()
}
