package com.expense.tracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

object CategoryIcons {
    fun getIcon(iconName: String): ImageVector =
        when (iconName) {
            "restaurant" -> Icons.Default.Restaurant
            "directions_car" -> Icons.Default.DirectionsCar
            "shopping_bag" -> Icons.Default.ShoppingBag
            "medical_services" -> Icons.Default.MedicalServices
            "movie" -> Icons.Default.Movie
            "home" -> Icons.Default.Home
            "book" -> Icons.Default.Book
            "payments" -> Icons.Default.Payments
            "lightbulb" -> Icons.Default.Lightbulb
            "inventory" -> Icons.Default.Inventory
            else -> Icons.Default.Category
        }
}
