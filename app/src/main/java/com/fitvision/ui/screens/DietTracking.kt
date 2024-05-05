package com.fitvision.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.fitvision.models.AddedFood
import com.fitvision.models.Food
import com.fitvision.models.FoodViewModel

// Composable function for displaying diet tracking screen
@Composable
fun DietTracking(viewModel: FoodViewModel, navController: NavController) {
    val availableFoods by viewModel.availableFoods.observeAsState(listOf())
    val totalCaloriesConsumed by viewModel.totalCaloriesConsumed.observeAsState(0)
    val addedFoods by viewModel.addedFoods.observeAsState(listOf())

    // Determining text color based on total calories consumed
    val textColor = when {
        totalCaloriesConsumed <= 2200 -> Color(0xFFFFFFE0)
        totalCaloriesConsumed <= 2400 -> Color(0xFFFFFF00)
        totalCaloriesConsumed <= 2600 -> Color(0xFFFFD700)
        totalCaloriesConsumed <= 2900 -> Color(0xFFFFA500)
        else -> Color.Red
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .padding(top = 16.dp, bottom = 90.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            IconButton(
                onClick = { navController.navigate("tracking") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    "Total Calories Consumed",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    "$totalCaloriesConsumed kcal",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(Modifier.size(33.dp))
        }
        AddedFoodsSummary(addedFoods, viewModel)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableFoods) { food ->
                FoodCard(
                    food = food,
                    onAddClick = { viewModel.addFoodToDiet(food) },
                    onRemoveClick = { viewModel.removeFoodFromDiet(food.id) }
                )
            }
        }
    }
}

// Composable function for displaying summary of added foods
@Composable
fun AddedFoodsSummary(addedFoods: List<AddedFood>, viewModel: FoodViewModel) {
    val groupedFoods = addedFoods.groupBy { it.foodId }
    val scrollState = rememberLazyListState()

    Column(modifier = Modifier.padding(0.dp)) {


        LazyRow(
            state = scrollState,
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .fillMaxWidth()
        ) {
            items(groupedFoods.entries.toList()) { entry ->
                val foodId = entry.key
                val food = viewModel.getFoodById(foodId)
                val count = entry.value.size
                if (food != null) {
                    Card(
                        modifier = Modifier.padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberAsyncImagePainter(food.imageUrl),
                                contentDescription = "Food Image",
                                modifier = Modifier
                                    .size(70.dp, 70.dp)
                                    .padding(end = 8.dp),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = "${food.name} x$count",
                                modifier = Modifier
                                    .size(100.dp, 20.dp),
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// Composable function for displaying food card
@Composable
fun FoodCard(food: Food, onAddClick: () -> Unit, onRemoveClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 5.dp),
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(food.imageUrl),
                    contentDescription = "Food Image",
                    modifier = Modifier
                        .size(100.dp, 100.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp, start = 16.dp)
                ) {
                    Text(text = food.name, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("${food.calories} kcal", color = Color.White)
                }

                Row(
                    modifier = Modifier
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onAddClick,
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF212121), CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    }
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF212121), CircleShape)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.White)
                    }
                }
            }
        }
    }
}