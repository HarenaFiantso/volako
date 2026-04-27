package com.expense.tracker.core.di

import com.expense.tracker.core.domain.repository.BudgetRepository
import com.expense.tracker.core.domain.repository.CategoryRepository
import com.expense.tracker.core.domain.repository.ExpenseRepository
import com.expense.tracker.features.expense.data.repository.BudgetRepositoryImpl
import com.expense.tracker.features.expense.data.repository.CategoryRepositoryImpl
import com.expense.tracker.features.expense.data.repository.ExpenseRepositoryImpl
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
    abstract fun bindExpenseRepository(impl: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository
}
