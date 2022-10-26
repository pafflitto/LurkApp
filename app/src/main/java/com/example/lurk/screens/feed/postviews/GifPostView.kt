package com.example.lurk.screens.feed.postviews

import android.util.Log
import android.view.LayoutInflater
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lurk.R
import com.example.lurk.extensions.MonitorProgressUpdates
import com.example.lurk.screens.feed.expanded_media_screen.ExpandedMedia
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun GifPostView(
    post: GifPost,
    exoPlayer: ExoPlayer? = null,
    expandMedia: (ExpandedMedia) -> Unit = {},
    showBlank: Boolean
) {
    val progress = remember { mutableStateOf(0f) }
    val isDragging = remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(progress.value)
    val context = LocalContext.current
    val density = LocalDensity.current

    var gifHeight by remember { mutableStateOf(0.dp) }

    exoPlayer.MonitorProgressUpdates(progress = progress, isDragging = isDragging)

    Box(
        Modifier
            .animateContentSize()
            .heightIn(0.dp, 500.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clipToBounds()
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = post.thumbnail),
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
        AnimatedVisibility(
            visible = exoPlayer != null && !showBlank,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            var playerView by remember { mutableStateOf<StyledPlayerView?>(null) }
            val hapticFeedback = LocalHapticFeedback.current
            AndroidView(
                modifier = Modifier
                    .height(gifHeight)
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                exoPlayer?.pause()
                                isDragging.value = true
                            },
                            onDragEnd = {
                                Log.e("DRAG", "Drag End")
                                exoPlayer?.play()
                                isDragging.value = false
                            },
                            onDragCancel = {
                                Log.e("DRAG", "Drag Cancel")
                                exoPlayer?.play()
                                isDragging.value = false
                            },
                            onDrag = { change, dragAmount ->
                                progress.value += dragAmount.x / 1000
                                val newProgress = (exoPlayer?.duration ?: 1L) * progress.value
                                exoPlayer?.seekTo(newProgress.toLong())
                            }
                        )
                    }
                    .clickable {
                        exoPlayer?.let {
                            expandMedia(ExpandedMedia(post = post, exoPlayer = it))
                        }
                    }
                    .align(Alignment.Center),
                factory = {
                    val layout =
                        LayoutInflater.from(context).inflate(R.layout.playerview, null, false)
                    playerView = layout.findViewById(R.id.player_view)
                    playerView?.apply {
                        player = exoPlayer
                    }

                    playerView!!
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

        AnimatedVisibility(
            visible = showBlank,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .height(gifHeight)
            )
        }
    }
}