package com.expense.tracker.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.domain.model.MonthlySummary
import com.expense.tracker.core.domain.usecase.GetMonthlySummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMonthlySummaryUseCase: GetMonthlySummaryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    @RequiresApi(Build.VERSION_CODES.O)
    private val _selectedMonth = MutableStateFlow(YearMonth.now())

    @RequiresApi(Build.VERSION_CODES.O)
    val monthlySummary: StateFlow<MonthlySummary?> =
        _selectedMonth.combine(_selectedMonth) { month, _ -> month }.let { monthFlow ->
                getMonthlySummaryUseCase(YearMonth.now()).stateIn(
                    scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
                )
            }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SelectMonth -> _selectedMonth.update { event.yearMonth }
            HomeEvent.DismissError -> _uiState.update { it.copy(error = null) }
            else -> {}
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false, val error: String? = null
)

sealed interface HomeEvent {
    data class DeleteExpense(val expenseId: Long) : HomeEvent
    data class SelectMonth(val yearMonth: YearMonth) : HomeEvent
    data object DismissError : HomeEvent
}
