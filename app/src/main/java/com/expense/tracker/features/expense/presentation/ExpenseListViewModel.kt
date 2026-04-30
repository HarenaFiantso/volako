package com.expense.tracker.features.expense.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.domain.model.Expense
import com.expense.tracker.core.domain.usecase.GetAllExpensesUseCase
import com.expense.tracker.core.domain.usecase.SearchExpensesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

class ExpenseListViewModel
    @Inject
    constructor(
        private val getAllExpensesUseCase: GetAllExpensesUseCase,
        private val searchExpensesUseCase: SearchExpensesUseCase,
    ) : ViewModel() {
        private val searchQuery = MutableStateFlow("")

        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        private val expensesFlow =
            searchQuery.debounce(300).distinctUntilChanged().flatMapLatest { query ->
                if (query.isBlank()) {
                    getAllExpensesUseCase()
                } else {
                    searchExpensesUseCase(query)
                }
            }

        val uiState: StateFlow<ExpenseListUiState> =
            combine(
                searchQuery,
                expensesFlow,
            ) { query, expenses ->
                val grouped: Map<LocalDate, List<Expense>> = expenses.sortedByDescending { it.date }.groupBy { it.date }

                ExpenseListUiState(
                    searchQuery = query,
                    groupedExpenses = grouped,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ExpenseListUiState(),
            )

        fun onEvent(event: ExpenseListEvent) {
            when (event) {
                is ExpenseListEvent.SearchQueryChanged -> searchQuery.update { event.query }
            }
        }
    }

data class ExpenseListUiState(
    val searchQuery: String = "",
    val groupedExpenses: Map<LocalDate, List<Expense>> = emptyMap(),
)

sealed interface ExpenseListEvent {
    data class SearchQueryChanged(
        val query: String,
    ) : ExpenseListEvent
}
