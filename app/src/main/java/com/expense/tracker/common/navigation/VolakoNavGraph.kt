package com.expense.tracker.common.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.expense.tracker.features.expense.presentation.AddEditExpenseScreen
import com.expense.tracker.features.expense.presentation.ExpenseDetailScreen
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val hideBottomBar =
        currentRoute in
            listOf(
                VolakoDestinations.ADD_EXPENSE,
                VolakoDestinations.EDIT_EXPENSE,
                VolakoDestinations.EXPENSE_DETAIL,
            )

    val showFab = currentRoute == VolakoDestinations.HOME

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { navController.navigate(VolakoDestinations.ADD_EXPENSE) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            if (!hideBottomBar) {
                GlassBottomBar(
                    items = bottomNavItems,
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = VolakoDestinations.HOME,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(VolakoDestinations.HOME) {
                HomeScreen(
                    onNavigateToExpenseDetail = { id ->
                        navController.navigate(VolakoDestinations.expenseDetail(id))
                    },
                )
            }
            composable(VolakoDestinations.ALL_EXPENSES) { Text("Expenses") }
            composable(VolakoDestinations.BUDGET) { Text("Budget") }
            composable(VolakoDestinations.CATEGORIES) { Text("Categories") }
            composable(VolakoDestinations.SETTINGS) { Text("Settings") }
            composable(VolakoDestinations.ADD_EXPENSE) {
                AddEditExpenseScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(
                route = VolakoDestinations.EXPENSE_DETAIL,
                arguments = listOf(navArgument(VolakoDestinations.ARG_EXPENSE_ID) { type = NavType.LongType }),
            ) {
                ExpenseDetailScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { id -> navController.navigate(VolakoDestinations.editExpense(id)) },
                )
            }
            composable(
                route = VolakoDestinations.EDIT_EXPENSE,
                arguments = listOf(navArgument(VolakoDestinations.ARG_EXPENSE_ID) { type = NavType.LongType }),
            ) {
                AddEditExpenseScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun GlassBottomBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (String) -> Unit,
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier =
                            Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) {
                                        primaryColor.copy(alpha = 0.12f)
                                    } else {
                                        Color.Transparent
                                    },
                                ).clickable { onItemClick(item.route) },
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint =
                                if (isSelected) {
                                    primaryColor
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                },
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }
            }
        }
    }
}
