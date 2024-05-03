package com.fitvision.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object MoodTracking : BottomBarScreen(
        route = "moodtracking",
        title = "Mood",
        icon = Icons.Default.Person
    )

    object Favorites : BottomBarScreen(
        route = "favorites",
        title = "Favorites",
        icon = Icons.Default.Favorite
    )

    object CalorieTracking : BottomBarScreen(
        route = "tracking",
        title = "Tracking",
        icon = Icons.Default.List
    )
    object Detail : BottomBarScreen(
        route = "detail/{exerciseId}",
        title = "Detail",
        icon = Icons.Default.Settings
    ) {
        fun createRoute(exerciseId: Int) = route.replace("{exerciseId}", exerciseId.toString())
    }

}