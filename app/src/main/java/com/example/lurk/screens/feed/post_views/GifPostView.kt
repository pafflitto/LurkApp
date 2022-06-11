package com.example.lurk.screens.feed.post_views

import android.view.LayoutInflater
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lurk.screens.feed.GifPost
import com.example.lurk.screens.feed.Post
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay

@Composable
fun GifPostView(
    modifier: Modifier = Modifier,
    post: GifPost,
    exoPlayer: ExoPlayer? = null,
    expandMedia: (Post) -> Unit = {},
) {
    var progress by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(progress)
    val context = LocalContext.current
    val density = LocalDensity.current

    var gifHeight by remember { mutableStateOf(0.dp) }

    exoPlayer?.let {
        LaunchedEffect(Unit) {
            while (true) {
                if (exoPlayer.playbackState == Player.STATE_READY && !isDragging) {
                    var loopDelay = 0f
                    loopDelay = 20 - (progress % 20)
                    if (loopDelay < 0) {
                        loopDelay += 20
                    }

                    progress = exoPlayer.currentPosition / exoPlayer.duration.toFloat()
                    delay(loopDelay.toLong())
                }
                else delay(500)
            }
        }
    }

    Box(
        Modifier
            .animateContentSize()
            .heightIn(0.dp, 500.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        post.thumbnail?.let { thumbnail ->
            Image(
                painter = rememberDrawablePainter(drawable = thumbnail),
                contentDescription = "Gif Thumbnail",
                contentScale = ContentScale.FillWidth,
                modifier =
                Modifier
                    .heightIn(0.dp, 500.dp)
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        gifHeight = with(density) { it.size.height.toDp() }
                    }
            )
        }
        AnimatedVisibility(
            visible = exoPlayer != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
                AndroidView(
                    modifier = Modifier
                        .height(gifHeight)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    isDragging = false
                                    exoPlayer?.play()
                                },
                                onDragStart = {
                                    exoPlayer?.pause()
                                },
                                onHorizontalDrag = { _, dragAmount ->
                                    isDragging = true
                                    val dragAmountTranslated = dragAmount / 1000
                                    progress += dragAmountTranslated
                                    exoPlayer?.seekTo(progress.toLong() * exoPlayer.duration)
                                }
                            )
                        }
                        .align(Alignment.Center),
                    factory = {
                        val layout = LayoutInflater.from(context)
                            .inflate(com.example.lurk.R.layout.playerview, null, false)
                        val playerView = layout.findViewById<StyledPlayerView>(com.example.lurk.R.id.player_view)
                        playerView.apply {
                            player = exoPlayer
                        }
                    }
                )
            }
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align(Alignment.BottomCenter)
            )
    }
}

enum class GifLoadingState {
    WAITING,
    LOADING,
    LOADED,
    ERROR,
}