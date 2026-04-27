package com.expense.tracker.features.expense.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expense.tracker.core.data.local.entity.TransactionType

@Composable
fun TransactionTypeToggle(
    selected: TransactionType,
    onTypeSelected: (TransactionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val incomeColor = Color(0xFF2E7D32)
    val expenseColor = Color(0xFFC62828)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp),
    ) {
        TransactionType.entries.forEach { type ->
            val isSelected = selected == type

            val backgroundColor by animateColorAsState(
                targetValue =
                    when {
                        !isSelected -> Color.Transparent
                        type == TransactionType.INCOME -> incomeColor
                        else -> expenseColor
                    },
                animationSpec = tween(durationMillis = 200),
                label = "bg_$type",
            )

            val textColor by animateColorAsState(
                targetValue =
                    when {
                        !isSelected -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> Color.White
                    },
                animationSpec = tween(durationMillis = 200),
                label = "text_$type",
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(backgroundColor)
                        .clickable { onTypeSelected(type) }
                        .padding(vertical = 12.dp),
            ) {
                Text(
                    text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = textColor,
                )
            }
        }
    }
}
