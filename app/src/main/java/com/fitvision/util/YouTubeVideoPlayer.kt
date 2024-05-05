package com.fitvision.util

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

// Composable function to display a YouTube video player
@Composable
fun YouTubeVideoPlayer(videoId: String) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    AndroidView(
        factory = { ctx ->
            YouTubePlayerView(ctx).apply {
                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        // Load the YouTube video with the given video id
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })
                lifecycle.addObserver(this)
            }
        },
        modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)
    )
}
