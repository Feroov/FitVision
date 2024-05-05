package com.fitvision.components

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fitvision.models.ExerciseViewModel
import com.fitvision.models.FoodViewModel
import com.fitvision.models.MoodViewModel
import com.fitvision.ui.screens.CalorieTracking
import com.fitvision.ui.screens.DetailScreen
import com.fitvision.ui.screens.DietTracking
import com.fitvision.ui.screens.HomeScreen
import com.fitvision.ui.screens.MoodTrack
import com.fitvision.ui.screens.Favorites

// Composable function for the bottom navigation graph
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
            val moodViewModel: MoodViewModel = viewModel()
            MoodTrack(viewModel = moodViewModel, navController)
        }

        composable(route = BottomBarScreen.Favorites.route) {
            val exerciseViewModel: ExerciseViewModel = viewModel()
            Favorites(viewModel = exerciseViewModel)
        }

        composable(route = BottomBarScreen.CalorieTracking.route) {
            val viewModel: MoodViewModel = viewModel()
            val foodViewModel: FoodViewModel = viewModel()
            CalorieTracking(viewModel, navController, foodViewModel)
        }

        composable(route = "dietTracking") {
            val foodViewModel: FoodViewModel = viewModel()
            DietTracking(foodViewModel, navController)
        }

        composable(
            route = BottomBarScreen.Detail.route,
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val exerciseViewModel: ExerciseViewModel = viewModel()
            val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: -1
            val exercise = exerciseViewModel.getExerciseById(exerciseId)
            if (exercise != null) {
                DetailScreen(navController, exercise, viewModel = exerciseViewModel)
            }
        }
    }
}

