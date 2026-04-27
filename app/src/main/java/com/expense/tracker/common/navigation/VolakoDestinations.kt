package com.expense.tracker.common.navigation

object VolakoDestinations {
    const val ARG_EXPENSE_ID = "expenseId"

    const val HOME = "home"
    const val ALL_EXPENSES = "all_expenses"
    const val BUDGET = "budget"
    const val CATEGORIES = "categories"
    const val SETTINGS = "settings"

    const val ADD_EXPENSE = "add_expense"
    const val EDIT_EXPENSE = "edit_expense/{$ARG_EXPENSE_ID}"
    const val EXPENSE_DETAIL = "expense_detail/{$ARG_EXPENSE_ID}"

    fun editExpense(id: Long) = "edit_expense/$id"

    fun expenseDetail(id: Long) = "expense_detail/$id"
}
