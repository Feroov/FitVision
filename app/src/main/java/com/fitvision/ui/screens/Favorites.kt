package com.fitvision.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.fitvision.models.ExerciseViewModel
import com.fitvision.models.FavoriteExercise


@Composable
fun Favorites(viewModel: ExerciseViewModel) {
    val favorites by viewModel.favoritesFlow.collectAsState(initial = listOf())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Favorites",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(           modifier = Modifier
            .padding(bottom = 90.dp)
        ) {
            items(favorites) { favorite ->
                FavoriteExerciseCard(favorite = favorite, onRemove = {
                    viewModel.removeFavorite(favorite)
                })
            }
        }
    }
}


@Composable
fun FavoriteExerciseCard(favorite: FavoriteExercise, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF76ABAE))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Image and Text
            Image(
                painter = rememberAsyncImagePainter(favorite.imageUrl),
                contentDescription = "Favorite Exercise Image",
                modifier = Modifier
                    .size(150.dp, 150.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.FillWidth
            )

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = favorite.name, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = favorite.category, color = Color.Black)
            }

            // Right side: Delete Button
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from Favorites",
                    tint = Color.Red
                )
            }
        }
    }
}

