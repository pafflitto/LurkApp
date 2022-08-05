package com.example.lurk.screens.expanded_media_screen

import android.view.LayoutInflater
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import com.example.lurk.screens.feed.GifPost
import com.example.lurk.screens.feed.ImagePost
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.exoplayer2.ui.StyledPlayerView

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
                    val context = LocalContext.current
                    AndroidView(modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                        factory = {
                            val layout = LayoutInflater.from(context).inflate(R.layout.playerview, null, false)
                            playerView = layout.findViewById(R.id.player_view)
                            playerView?.apply {
                               player = expandedMedia.exoPlayer
                            }
                            playerView!!
                        }
                    )
                }
            }
        }
    }
}