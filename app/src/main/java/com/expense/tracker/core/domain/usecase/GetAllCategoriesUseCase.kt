package com.expense.tracker.core.domain.usecase

import com.expense.tracker.core.domain.model.Category
import com.expense.tracker.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase
    @Inject
    constructor(
        private val categoryRepository: CategoryRepository,
    ) {
        operator fun invoke(): Flow<List<Category>> = categoryRepository.observeAll()
    }
