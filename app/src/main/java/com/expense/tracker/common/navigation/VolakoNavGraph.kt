package com.expense.tracker.common.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.expense.tracker.features.home.HomeScreen

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val bottomNavItems =
    listOf(
        BottomNavItem(VolakoDestinations.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(
            VolakoDestinations.ALL_EXPENSES,
            "Expenses",
            Icons.AutoMirrored.Filled.List,
            Icons.AutoMirrored.Outlined.List,
        ),
        BottomNavItem(
            VolakoDestinations.BUDGET,
            "Budget",
            Icons.Filled.AccountBalanceWallet,
            Icons.Outlined.AccountBalanceWallet,
        ),
        BottomNavItem(VolakoDestinations.CATEGORIES, "Categories", Icons.Filled.Category, Icons.Outlined.Category),
        BottomNavItem(VolakoDestinations.SETTINGS, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings),
    )

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VolakoNavGraph() {
    val navController = rememberNavController()
    val navBackStackSentry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackSentry?.destination?.route

    val hideBottomBar =
        currentRoute in
            listOf(
                VolakoDestinations.ADD_EXPENSE,
                VolakoDestinations.EDIT_EXPENSE,
                VolakoDestinations.EXPENSE_DETAIL,
            )

    val showFab = currentRoute == VolakoDestinations.HOME

    Scaffold(
        bottomBar = {
            if (!hideBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                )
                            },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { navController.navigate(VolakoDestinations.ADD_EXPENSE) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = VolakoDestinations.HOME,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(VolakoDestinations.HOME) {
                HomeScreen(
                    onNavigateToExpenseDetail = { id -> navController.navigate(VolakoDestinations.expenseDetail(id)) },
                )
            }
            composable(VolakoDestinations.ALL_EXPENSES) { Text("Expenses") }
            composable(VolakoDestinations.BUDGET) { Text("Budget") }
            composable(VolakoDestinations.CATEGORIES) { Text("Categories") }
            composable(VolakoDestinations.SETTINGS) { Text("Settings") }
        }
    }
}
