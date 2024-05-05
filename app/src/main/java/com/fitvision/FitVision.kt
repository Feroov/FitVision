package com.fitvision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import com.fitvision.ui.screens.SplashScreen
import kotlinx.coroutines.delay
class FitVision : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSplashScreen by remember { mutableStateOf(true) }

            LaunchedEffect(key1 = true) {
                delay(1000)
                showSplashScreen = false
            }

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