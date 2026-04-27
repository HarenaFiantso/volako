package com.expense.tracker.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expense.tracker.ui.components.ExpenseCard
import com.expense.tracker.ui.components.SectionHeader
import com.expense.tracker.ui.components.SummaryCard
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddExpense: () -> Unit,
    onNavigateToExpenseDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
) {
    val summary by viewModel.monthlySummary.collectAsStateWithLifecycle()
    val expenses by viewModel.recentExpenses.collectAsStateWithLifecycle()

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Volako",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddExpense,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                MonthNavigator(currentMonth = currentMonth, onPreviousMonth = {
                    currentMonth = currentMonth.minusMonths(1)
                    viewModel.onEvent(HomeEvent.SelectMonth(currentMonth))
                }, onNextMonth = {
                    currentMonth = currentMonth.plusMonths(1)
                    viewModel.onEvent(HomeEvent.SelectMonth(currentMonth))
                })
            }
            summary?.let { monthlySummary ->
                item {
                    BalanceCard(summary = monthlySummary)
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        SummaryCard(
                            label = "Income",
                            amount = monthlySummary.totalIncome,
                            isIncome = true,
                            modifier = Modifier.weight(1f),
                        )
                        SummaryCard(
                            label = "Expenses",
                            amount = monthlySummary.totalExpenses,
                            isIncome = false,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                item {
                    SectionHeader(title = "Recent Transactions")
                }
                if (expenses.isEmpty()) {
                    item {
                        EmptyState()
                    }
                } else {
                    items(
                        items = expenses,
                        key = { it.id },
                    ) { expense ->
                        ExpenseCard(
                            expense = expense,
                            onClick = { onNavigateToExpenseDetail(expense.id) },
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}
