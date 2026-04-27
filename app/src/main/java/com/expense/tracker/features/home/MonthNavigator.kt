package com.expense.tracker.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.expense.tracker.common.extensions.toDisplayString
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthNavigator(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous month")
        }
        AnimatedContent(targetState = currentMonth, label = "month_label") { month ->
            Text(
                text = month.toDisplayString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
        IconButton(
            onClick = onNextMonth,
            enabled = currentMonth < YearMonth.now(),
        ) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Next month")
        }
    }
}
