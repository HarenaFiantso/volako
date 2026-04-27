package com.expense.tracker.features.expense.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expense.tracker.core.data.local.entity.TransactionType

@Composable
fun TransactionTypeToggle(
    selected: TransactionType,
    onTypeSelected: (TransactionType) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TransactionType.entries.forEach { type ->
            FilterChip(
                selected = selected == type,
                onClick = { onTypeSelected(type) },
                label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}
