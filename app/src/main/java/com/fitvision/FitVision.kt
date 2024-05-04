package com.fitvision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fitvision.ui.screens.HomeScreen
import com.fitvision.ui.theme.FitVisionTheme


class FitVision : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitVisionTheme {
                MainScreen()
            }
        }
    }
}


