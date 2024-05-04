package com.fitvision.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fitvision.models.FoodViewModel
import com.fitvision.models.MoodViewModel
import java.util.Locale

@Composable
fun CalorieTracking(viewModel: MoodViewModel, navController: NavHostController, foodViewModel: FoodViewModel) {
    val moodEntries by viewModel.getAllMoods().collectAsState(initial = listOf())
    val totalCaloriesBurned = moodEntries.sumOf { it.calories }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .padding(30.dp)
    ) {
        Text(
            text = "Calorie Tracking",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Total estimated burnt kcal: $totalCaloriesBurned",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(moodEntries) { entry ->
                val textColor = if (entry.calories >= 500) Color.Red else Color.Black // Highlight if calories burned are 500 or more

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
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
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(31.dp)
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    append("${formatDateString(entry.date.toString())}: Estimated burnt calories: ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                                        append("${entry.calories} kcal")
                                    }
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 70.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { navController.navigate("dietTracking") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Diet Tracking", color = Color.Black)
                }
            }
        }
    }
}


fun formatDateString(dateString: String): String {
    val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
    val date = sdf.parse(dateString)
    val dateFormat = SimpleDateFormat("EEE MMM dd yyyy", Locale.ENGLISH)
    return dateFormat.format(date!!)
}