package com.expense.tracker.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    @RequiresApi(Build.VERSION_CODES.O)
    private val _selectedMonth = MutableStateFlow(YearMonth.now())

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
