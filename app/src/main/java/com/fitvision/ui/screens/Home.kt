package com.fitvision.ui.screens

import com.fitvision.R
import com.fitvision.components.BottomBarScreen
import com.fitvision.models.Exercise
import com.fitvision.models.ExerciseViewModel
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.fitvision.models.FavoriteExercise
import com.fitvision.util.YouTubeVideoPlayer
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(viewModel: ExerciseViewModel, navController: NavHostController) {

    var searchText by remember { mutableStateOf("") }
    val exercises by viewModel.exercises.observeAsState(initial = emptyList())

    val imageResourceIds = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4
    )

    // Index to keep track of the current image in the carousel
    var currentIndex by remember { mutableStateOf(0) }

    // Update the current index in the carousel
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % imageResourceIds.size
        }
    }

    // Create a list of filtered exercises based on the search text
    val filteredExercises = exercises.filter {
        searchText.isEmpty() || it.name.contains(searchText, ignoreCase = true) ||
                it.category.contains(searchText, ignoreCase = true)
    }

    // Define navigation bar height for padding at the bottom of the LazyColumn
    val navigationBarHeight = 90.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
    ) {
        // Logo image
        Image(
            painter = painterResource(id = R.drawable.fitvisiontransparent),
            contentDescription = "FitVision Logo",
            modifier = Modifier
                .fillMaxWidth()
                .size(width = 100.dp, height = 90.dp)
        )

        // Carousel of images
        AnimatedContent(
            targetState = imageResourceIds[currentIndex],
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(600)
                ) with slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(600)
                )
            }
        ) { targetImage ->
            Image(
                painter = painterResource(id = targetImage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 7f),
                contentScale = ContentScale.FillWidth
            )
        }

        // Search bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("Search") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF212121),
                unfocusedContainerColor = Color(0xFF212121),
                cursorColor = Color.White,
                focusedBorderColor = Color(0xFF76ABAE),
                unfocusedBorderColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color.Gray
            ),
            shape = MaterialTheme.shapes.medium
        )

        // List of filtered exercises
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = navigationBarHeight)
        ) {
            items(filteredExercises) { exercise ->
                ExerciseCard(exercise) {
                    navController.navigate(BottomBarScreen.Detail.createRoute(exercise.id))
                }
            }
        }
    }
}


@Composable
fun ExerciseCard(exercise: Exercise, navigateToDetail: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clickable(onClick = navigateToDetail),
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
                    .padding(horizontal = 20.dp, vertical = 1.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left side - Text content
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = exercise.name, fontWeight = FontWeight.Bold)
                    Text(text = exercise.category)
                }

                // Right side - Image
                Image(
                    painter = rememberAsyncImagePainter(model = exercise.imageUrl),
                    contentDescription = "Exercise Image",
                    modifier = Modifier
                        .size(130.dp, 90.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun DetailScreen(navController: NavHostController, exercise: Exercise, viewModel: ExerciseViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val favoriteEvent by viewModel.favoriteAdded.collectAsState(initial = "")

    val favoriteExercise = FavoriteExercise(
        id = exercise.id,
        name = exercise.name,
        category = exercise.category,
        description = exercise.description,
        imageUrl = exercise.imageUrl,
        videoUrl = exercise.videoUrl
    )

    LaunchedEffect(favoriteEvent) {
        if (favoriteEvent.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = favoriteEvent,
                duration = SnackbarDuration.Short
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xFF212121))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = exercise.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(3f)
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    viewModel.addFavorite(favoriteExercise)
                }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Add to Favorites",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            YouTubeVideoPlayer(videoId = YouTubeUrlParser.parse(exercise.videoUrl))
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3e3e42))
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = exercise.description,
                            color = Color.White,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberAsyncImagePainter(exercise.imageUrl),
                contentDescription = "Exercise Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(50.dp)
        ) {
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}


object YouTubeUrlParser {
    fun parse(url: String): String {
        // Regex to extract the YouTube video ID from a URL
        return Regex("v=([a-zA-Z0-9_\\-]+)").find(url)?.groups?.get(1)?.value ?: ""
    }
}
