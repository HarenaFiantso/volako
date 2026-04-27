package com.expense.tracker.core.di

import android.content.Context
import androidx.room.Room
import com.expense.tracker.core.data.local.VolakoDatabase
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.dao.ExpenseDao
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
    fun provideVolakoDatabase(
        @ApplicationContext context: Context,
    ): VolakoDatabase =
        Room
            .databaseBuilder(
                context,
                VolakoDatabase::class.java,
                "volako_db",
            ).build()

    @Provides
    fun provideExpenseDao(database: VolakoDatabase): ExpenseDao = database.expenseDao()

    @Provides
    fun provideCategoryDao(database: VolakoDatabase): CategoryDao = database.categoryDao()
}
