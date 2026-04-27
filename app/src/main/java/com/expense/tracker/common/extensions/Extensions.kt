package com.expense.tracker.common.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Double.formatAsCurrency(currencyCode: String = "USD"): String {
    val format =
        NumberFormat.getCurrencyInstance().apply {
            currency = java.util.Currency.getInstance(currencyCode)
        }
    return format.format(this)
}

@RequiresApi(Build.VERSION_CODES.O)
fun YearMonth.toDisplayString(): String = format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
