package com.expense.tracker.core.di

import com.expense.tracker.core.data.repository.CategoryRepositoryImpl
import com.expense.tracker.core.data.repository.ExpenseRepositoryImpl
import com.expense.tracker.core.domain.repository.CategoryRepository
import com.expense.tracker.core.domain.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindExpenseRepository(expenseRepositoryImpl: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository
}
