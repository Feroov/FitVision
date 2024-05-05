package com.fitvision.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitvision.models.MoodEntry
import com.fitvision.models.MoodViewModel
import java.text.SimpleDateFormat
import java.util.*

// Composable function to display the MoodTrack screen
@Composable
fun MoodTrack(viewModel: MoodViewModel, navController: NavController) {
    val moodEntries by viewModel.getAllMoods().collectAsState(initial = listOf())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                "How was the gym?",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(48.dp))
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF333333)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                listOf("Awesome", "Okay", "Bad").forEach { mood ->
                    MoodButton(mood, onClick = {
                        viewModel.insertMood(mood, "Logged on ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())}")
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 90.dp)
        ) {
            items(moodEntries) { entry ->
                MoodEntryCard(moodEntry = entry, onRemove = { viewModel.deleteMood(entry) })
            }
        }
    }
}

// Composable function to display a mood button
@Composable
fun MoodButton(mood: String, onClick: () -> Unit) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 4.dp, horizontal = 10.dp)
    ) {
        Text(mood, color = Color.Black, fontSize = 27.sp)
    }
}

// Composable function to display a mood entry card
@SuppressLint("RememberReturnType")
@Composable
fun MoodEntryCard(moodEntry: MoodEntry, onRemove: () -> Unit) {

    val scale = remember { Animatable(1f) }

    LaunchedEffect(moodEntry.mood) {
        if (moodEntry.mood == "Awesome") {
            scale.animateTo(1.2f, animationSpec = tween(durationMillis = 500))
            scale.animateTo(1f, animationSpec = tween(durationMillis = 500))
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF76ABAE))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                            startY = 220f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = moodEntry.mood,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value
                        )
                    )
                    moodEntry.description?.let {
                        Text(it, color = Color.Black, fontSize = 14.sp)
                    }
                }
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF212121), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
