package com.fitvision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.fitvision.ui.screens.SplashScreen
import kotlinx.coroutines.delay

/**
 * The main entry point of the FitVision application.
 */
class FitVision : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSplashScreen by remember { mutableStateOf(true) }

            // Delay to simulate a splash screen
            LaunchedEffect(key1 = true) {
                delay(1000)
                showSplashScreen = false
            }

            // Crossfade animation to transition between splash screen and main screen
            Crossfade(
                targetState = showSplashScreen,
                animationSpec = tween(durationMillis = 2500)) { showSplash ->
                if (showSplash) {
                    SplashScreen()
                } else {
                    MainScreen()
                }
            }
        }
    }
}