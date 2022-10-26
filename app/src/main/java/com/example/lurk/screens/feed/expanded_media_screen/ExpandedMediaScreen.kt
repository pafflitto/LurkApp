package com.example.lurk.screens.feed.expanded_media_screen

import android.view.LayoutInflater
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lurk.R
import com.example.lurk.extensions.noIndicationClick
import com.example.lurk.screens.feed.postviews.GifPost
import com.example.lurk.screens.feed.postviews.ImagePost
import com.example.lurk.ui.components.PlayerView
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.exoplayer2.ui.StyledPlayerView

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandedMediaScreen(
    showExpanded: Boolean = false,
    expandedMedia: ExpandedMedia?,
    shrinkMedia: () -> Unit
) {
    val post = expandedMedia?.post
    val maxDragAmount = -500f
    var dragChangeAmount by remember { mutableStateOf(0f) }
    val alpha by animateFloatAsState(targetValue = 1 - (dragChangeAmount / maxDragAmount))
    val offsetChange by animateDpAsState(targetValue = with (LocalDensity.current) { dragChangeAmount.toDp()})
    val scale by animateFloatAsState(targetValue =  if (dragChangeAmount != 0f)  (1 - ((dragChangeAmount / 5) / maxDragAmount)) else 1f)
    AnimatedVisibility(
        visible = expandedMedia != null && showExpanded,
        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
        exit = fadeOut(tween(0))
    ) {
        Box(
            modifier = Modifier
                .alpha(alpha)
                .background(color = Color.Black)
                .offset(y = if (offsetChange > 0.dp) 0.dp else offsetChange)
                .scale(if (scale > 1f) 1f else scale)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragCancel = {
                            dragChangeAmount = 0f
                        },
                        onDragEnd = {
                            if (dragChangeAmount / maxDragAmount > 1) {
                                shrinkMedia()
                            }
                            dragChangeAmount = 0f
                        },
                        onVerticalDrag = { _, dragAmount ->
                            dragChangeAmount += dragAmount
                        }
                    )
                }
        ) {
            when(post) {
                is ImagePost -> {
                    Image(
                        painter = rememberDrawablePainter(drawable = post.image),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }
                is GifPost -> {
                    var playerView by remember { mutableStateOf<StyledPlayerView?>(null) }
                    var showPlayerView by remember { mutableStateOf(false) }
                    val context = LocalContext.current

                    Box(Modifier.noIndicationClick { showPlayerView = !showPlayerView }) {
                        AndroidView(modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                            factory = {
                                val layout = LayoutInflater.from(context)
                                    .inflate(R.layout.playerview, null, false)
                                playerView = layout.findViewById(R.id.player_view)
                                playerView?.apply {
                                    player = expandedMedia.exoPlayer
                                }
                                playerView!!
                            }
                        )
                        AnimatedVisibility(
                            visible = showPlayerView,
                            enter = scaleIn() + slideInVertically(
                                initialOffsetY = { it }
                            ),
                            exit = scaleOut() + slideOutVertically(
                                targetOffsetY = { it }
                            ),
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            PlayerView(
                                exoPlayer = expandedMedia.exoPlayer
                            )
                        }
                    }
                }
            }
        }
    }
}