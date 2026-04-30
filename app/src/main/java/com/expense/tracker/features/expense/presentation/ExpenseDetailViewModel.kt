package com.expense.tracker.features.expense.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.common.navigation.VolakoDestinations
import com.expense.tracker.core.domain.model.Expense
import com.expense.tracker.core.domain.usecase.DeleteExpenseUseCase
import com.expense.tracker.core.domain.usecase.GetExpenseByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        getExpenseByIdUseCase: GetExpenseByIdUseCase,
        private val deleteExpenseUseCase: DeleteExpenseUseCase,
    ) : ViewModel() {
        private val expenseId: Long = checkNotNull(savedStateHandle[VolakoDestinations.ARG_EXPENSE_ID])

        val uiState: StateFlow<ExpenseDetailUiState> =
            getExpenseByIdUseCase(expenseId)
                .map { expense -> ExpenseDetailUiState(expense = expense) }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ExpenseDetailUiState())

        private val _navigationEvent = MutableSharedFlow<ExpenseDetailNavEvent>()
        val navigationEvent = _navigationEvent.asSharedFlow()

        fun onEvent(event: ExpenseDetailEvent) {
            when (event) {
                ExpenseDetailEvent.DeleteConfirmed -> deleteExpense()
            }
        }

        private fun deleteExpense() {
            viewModelScope.launch {
                deleteExpenseUseCase(expenseId)
                _navigationEvent.emit(ExpenseDetailNavEvent.NavigateBack)
            }
        }
    }

data class ExpenseDetailUiState(
    val expense: Expense? = null,
    val isLoading: Boolean = false,
)

sealed interface ExpenseDetailEvent {
    data object DeleteConfirmed : ExpenseDetailEvent
}

sealed interface ExpenseDetailNavEvent {
    data object NavigateBack : ExpenseDetailNavEvent
}
