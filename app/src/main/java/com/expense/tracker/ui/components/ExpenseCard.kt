package com.expense.tracker.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.expense.tracker.common.extensions.formatAsCurrency
import com.expense.tracker.common.extensions.toRelativeString
import com.expense.tracker.common.theme.VolakoRed
import com.expense.tracker.core.data.local.entity.TransactionType
import com.expense.tracker.core.domain.model.Expense

@Composable
fun CategoryIconBadge(
    icon: String,
    colorHex: String,
    modifier: Modifier = Modifier,
    size: Int = 48,
) {
    val color = runCatching { Color(colorHex.toColorInt()) }.getOrDefault(MaterialTheme.colorScheme.surfaceVariant)

    Box(
        modifier =
            modifier
                .size(size.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = icon, style = MaterialTheme.typography.titleMedium)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseCard(
    expense: Expense,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CategoryIconBadge(
                icon = expense.category?.icon ?: "📦",
                colorHex = expense.category?.colorHex ?: "#C7C7C7",
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = expense.category?.name ?: "Uncategorized",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = expense.date.toRelativeString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            val amountColor =
                if (expense.type == TransactionType.INCOME) {
                    MaterialTheme.colorScheme.primary
                } else {
                    VolakoRed
                }
            val prefix = if (expense.type == TransactionType.INCOME) "+" else "-"

            Text(
                text = "$prefix${expense.amount.formatAsCurrency()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor,
            )
        }
    }
}
