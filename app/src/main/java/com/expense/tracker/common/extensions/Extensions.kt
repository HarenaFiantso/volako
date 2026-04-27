package com.expense.tracker.common.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun YearMonth.toDisplayString(): String = format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
