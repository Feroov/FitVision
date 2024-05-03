package com.fitvision.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitvision.models.MoodEntry
import com.fitvision.models.MoodViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MoodTrack(viewModel: MoodViewModel) {
    val moodEntries by viewModel.getAllMoods().collectAsState(initial = listOf())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF424242))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "How was your workout session?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("Awesome", "Okay", "Bad").forEach { mood ->
                        MoodButton(mood, onClick = {
                            viewModel.insertMood(mood, "Logged on ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())}")
                        })
                    }
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

@Composable
fun MoodButton(mood: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76ABAE)),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 5.dp)
    ) {
        Text(mood, color = Color.Black, fontSize = 26.sp)
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun MoodEntryCard(moodEntry: MoodEntry, onRemove: () -> Unit) {
    // Animation state
    val scale = remember { Animatable(1f) }

    LaunchedEffect(moodEntry.mood) {
        if (moodEntry.mood == "Awesome") {
            // Animate scale from 0.8 to 1.2 and back to 1
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
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}
