package com.fitvision.components


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fitvision.models.ExerciseViewModel
import com.fitvision.ui.screens.Analytics
import com.fitvision.ui.screens.DetailScreen
import com.fitvision.ui.screens.HomeScreen
import com.fitvision.ui.screens.MoodTrack
import com.fitvision.ui.screens.FavoritesScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {

            val exerciseViewModel: ExerciseViewModel = viewModel()

            HomeScreen(viewModel = exerciseViewModel, navController = navController)
        }
        composable(route = BottomBarScreen.MoodTracking.route) {
            MoodTrack()
        }

        composable(route = BottomBarScreen.Favorites.route) {
            FavoritesScreen()
        }

        composable(route = BottomBarScreen.Analytics.route) {
            Analytics()
        }

        composable(
            route = BottomBarScreen.Detail.route,
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val exerciseViewModel: ExerciseViewModel = viewModel()
            val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: -1
            val exercise = exerciseViewModel.getExerciseById(exerciseId)
            if (exercise != null) {
                DetailScreen(navController, exercise) // Now passing navController as well
            }
        }
    }
}