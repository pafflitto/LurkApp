package com.example.lurk.screens.feed.post_views

import android.view.LayoutInflater
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lurk.R
import com.example.lurk.screens.expanded_media_screen.ExpandedMedia
import com.example.lurk.screens.feed.GifPost
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GifPostView(
    post: GifPost,
    exoPlayer: ExoPlayer? = null,
    expandMedia: (ExpandedMedia) -> Unit = {},
    showBlank: Boolean
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
            AndroidView(
                modifier = Modifier
                    .height(gifHeight)
//                        .pointerInput(Unit) {
//                            detectHorizontalDragGestures(
//                                onDragEnd = {
//                                    isDragging = false
//                                    exoPlayer?.play()
//                                },
//                                onDragStart = {
//                                    exoPlayer?.pause()
//                                },
//                                onHorizontalDrag = { _, dragAmount ->
//                                    isDragging = true
//                                    val dragAmountTranslated = dragAmount / 1000
//                                    progress += dragAmountTranslated
//                                    exoPlayer?.seekTo(progress.toLong() * exoPlayer.duration)
//                                }
//                            )
//                            detectTapGestures {
//                                if (exoPlayer != null) {
//                                    playerView?.player = null
//                                    expandMedia(post, exoPlayer)
//                                }
//                            }
//                        }
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