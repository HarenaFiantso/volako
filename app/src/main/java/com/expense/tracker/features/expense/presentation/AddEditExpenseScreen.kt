package com.expense.tracker.features.expense.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expense.tracker.ui.components.LoadingOverlay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExpenseScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditExpenseViewModel = hiltViewModel<AddEditExpenseViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                AddEditNavEvent.SavedSuccessfully -> onNavigateBack()
                AddEditNavEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { snackbarHostState.showSnackbar(it) }
    }

    if (showDatePicker) {
        VolakoDatePickerDialog(selectedDate = uiState.selectedDate, onDateSelected = { date ->
            viewModel.onEvent(AddEditExpenseEvent.DateSelected(date))
        }, onDismiss = { })
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(if (uiState.isEditMode) "Edit Transaction" else "New Transaction")
            }, navigationIcon = {
                IconButton(onClick = { viewModel.onEvent(AddEditExpenseEvent.NavigateBack) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            }, actions = {
                IconButton(
                    onClick = { viewModel.onEvent(AddEditExpenseEvent.Save) },
                    enabled = !uiState.isSaving,
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Save")
                }
            })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TransactionTypeToggle(
                selected = uiState.transactionType,
                onTypeSelected = { viewModel.onEvent(AddEditExpenseEvent.TypeChanged(it)) },
            )
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = { viewModel.onEvent(AddEditExpenseEvent.AmountChanged(it)) },
                label = { Text("Amount") },
                isError = uiState.amountError != null,
                supportingText = uiState.amountError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("$") },
            )
            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.onEvent(AddEditExpenseEvent.TitleChanged(it)) },
                label = { Text("Title") },
                isError = uiState.titleError != null,
                supportingText = uiState.titleError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = uiState.note,
                onValueChange = { viewModel.onEvent(AddEditExpenseEvent.NoteChanged(it)) },
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )
            OutlinedTextField(
                value = uiState.selectedDate.toString(),
                onValueChange = {},
                readOnly = true,
                label = { Text("Date") },
                trailingIcon = {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Pick date")
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { },
            )
            Text("Category", style = MaterialTheme.typography.labelLarge)
            CategoryGrid(
                categories = categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { viewModel.onEvent(AddEditExpenseEvent.CategorySelected(it)) },
            )

            LoadingOverlay(isVisible = uiState.isSaving)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
