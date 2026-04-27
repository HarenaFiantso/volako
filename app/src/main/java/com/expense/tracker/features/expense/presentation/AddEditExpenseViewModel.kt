package com.expense.tracker.features.expense.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.common.navigation.VolakoDestinations
import com.expense.tracker.core.data.local.entity.TransactionType
import com.expense.tracker.core.domain.model.Category
import com.expense.tracker.core.domain.model.Expense
import com.expense.tracker.core.domain.usecase.GetAllCategoriesUseCase
import com.expense.tracker.core.domain.usecase.GetExpenseByIdUseCase
import com.expense.tracker.core.domain.usecase.SaveExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AddEditExpenseViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val saveExpenseUseCase: SaveExpenseUseCase,
        private val getExpenseByIdUseCase: GetExpenseByIdUseCase,
        getAllCategoriesUseCase: GetAllCategoriesUseCase,
    ) : ViewModel() {
        private val expenseId: Long? =
            savedStateHandle
                .get<Long>(
                    VolakoDestinations.ARG_EXPENSE_ID,
                )?.takeIf { it != -1L }

        @RequiresApi(Build.VERSION_CODES.O)
        private val _uiState = MutableStateFlow(AddEditExpenseUiState())
        val uiState: StateFlow<AddEditExpenseUiState> = _uiState.asStateFlow()

        private val _navigationEvent = MutableSharedFlow<AddEditNavEvent>()
        val navigationEvent = _navigationEvent.asSharedFlow()

        val categories: StateFlow<List<Category>> =
            getAllCategoriesUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

        init {
            expenseId?.let { loadExpense(it) }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun loadExpense(id: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                getExpenseByIdUseCase(id).collect { expense ->
                    expense?.let {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isEditMode = true,
                                title = it.title,
                                amount = it.amount.toString(),
                                note = it.note ?: "",
                                selectedCategory = it.category,
                                selectedDate = it.date,
                                transactionType = it.type,
                            )
                        }
                    }
                }
            }
        }

        fun onEvent(event: AddEditExpenseEvent) {
            when (event) {
                is AddEditExpenseEvent.TitleChanged ->
                    _uiState.update {
                        it.copy(
                            title = event.value,
                            titleError = null,
                        )
                    }

                is AddEditExpenseEvent.AmountChanged ->
                    _uiState.update {
                        it.copy(
                            amount = event.value,
                            amountError = null,
                        )
                    }

                is AddEditExpenseEvent.NoteChanged -> _uiState.update { it.copy(note = event.value) }
                is AddEditExpenseEvent.CategorySelected ->
                    _uiState.update {
                        it.copy(
                            selectedCategory = event.category,
                        )
                    }

                is AddEditExpenseEvent.DateSelected -> _uiState.update { it.copy(selectedDate = event.date) }
                is AddEditExpenseEvent.TypeChanged -> _uiState.update { it.copy(transactionType = event.type) }
                AddEditExpenseEvent.Save -> saveExpense()
                AddEditExpenseEvent.NavigateBack -> navigateBack()
            }
        }

        private fun saveExpense() {
            val state = _uiState.value
            val amountDouble = state.amount.toDoubleOrNull()

            var hasError = false
            if (state.title.isBlank()) {
                _uiState.update { it.copy(titleError = "Title is required") }
                hasError = true
            }
            if (amountDouble == null || amountDouble <= 0) {
                _uiState.update { it.copy(amountError = "Enter a valid amount") }
                hasError = true
            }
            if (hasError) return

            viewModelScope.launch {
                _uiState.update { it.copy(isSaving = true) }

                val expense =
                    Expense(
                        id = expenseId ?: 0L,
                        title = state.title.trim(),
                        amount = amountDouble!!,
                        note = state.note.trim().ifEmpty { null },
                        category = state.selectedCategory,
                        date = state.selectedDate,
                        type = state.transactionType,
                    )

                saveExpenseUseCase(expense)
                    .onSuccess { _navigationEvent.emit(AddEditNavEvent.SavedSuccessfully) }
                    .onFailure { error ->
                        _uiState.update { it.copy(isSaving = false, error = error.message) }
                    }
            }
        }

        private fun navigateBack() {
            viewModelScope.launch { _navigationEvent.emit(AddEditNavEvent.NavigateBack) }
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
data class AddEditExpenseUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditMode: Boolean = false,
    val title: String = "",
    val titleError: String? = null,
    val amount: String = "",
    val amountError: String? = null,
    val note: String = "",
    val selectedCategory: Category? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val error: String? = null,
)

sealed interface AddEditExpenseEvent {
    data class TitleChanged(
        val value: String,
    ) : AddEditExpenseEvent

    data class AmountChanged(
        val value: String,
    ) : AddEditExpenseEvent

    data class NoteChanged(
        val value: String,
    ) : AddEditExpenseEvent

    data class CategorySelected(
        val category: Category?,
    ) : AddEditExpenseEvent

    data class DateSelected(
        val date: LocalDate,
    ) : AddEditExpenseEvent

    data class TypeChanged(
        val type: TransactionType,
    ) : AddEditExpenseEvent

    data object Save : AddEditExpenseEvent

    data object NavigateBack : AddEditExpenseEvent
}

sealed interface AddEditNavEvent {
    data object SavedSuccessfully : AddEditNavEvent

    data object NavigateBack : AddEditNavEvent
}
