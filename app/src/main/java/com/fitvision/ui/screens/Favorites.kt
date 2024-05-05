package com.fitvision.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
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
            .padding(top = 30.dp)
    ) {
        Text(
            text = "Favorites",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 36.dp),
            color = Color.White,
            fontSize = 40.sp,
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
    val context = LocalContext.current

    fun shareExercise() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this exercise video: " +
                    "${favorite.name} - ${favorite.category}: ${favorite.videoUrl}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(context, shareIntent, null)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF76ABAE))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                            startY = 150f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(favorite.imageUrl),
                    contentDescription = "Favorite Exercise Image",
                    modifier = Modifier
                        .size(100.dp, 60.dp)
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    Text(text = favorite.name, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = favorite.category, color = Color.Black)
                }

                Row(

                ) {
                    IconButton(
                        onClick = { shareExercise() },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF212121), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                    Spacer(Modifier.width(9.dp))

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
}
