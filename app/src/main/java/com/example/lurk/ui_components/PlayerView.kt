package com.example.lurk.ui_components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward5
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay5
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.extensions.MonitorProgressUpdates
import com.example.lurk.extensions.noIndicationClick
import com.example.lurk.extensions.shrinkClick
import com.example.lurk.ui.theme.LurkTheme
import com.google.android.exoplayer2.ExoPlayer

@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer? = null
) {
    val duration = exoPlayer?.duration ?: 1
    val progress = remember { mutableStateOf(((exoPlayer?.currentPosition ?: 1) / duration).toFloat()) }
    val isDragging = remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(targetValue = progress.value)
    var videoPlaying by remember { mutableStateOf(exoPlayer?.isPlaying ?: true) }

    exoPlayer.MonitorProgressUpdates(
        progress = progress,
        isDragging = isDragging
    )

    LurkTheme(true) {
        Column(
            modifier = modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(28.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ButtonRow(
                exoPlayer = exoPlayer,
                videoPlaying = videoPlaying,
            ) {
                videoPlaying = it
            }
            Slider(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                value = animatedProgress,
                onValueChange = {
                    videoPlaying = false
                    isDragging.value = true
                    val newProgress = (exoPlayer?.duration ?: 0L) * it
                    exoPlayer?.pause()
                    progress.value = newProgress / (exoPlayer?.duration ?: 1)
                    exoPlayer?.seekTo(newProgress.toLong())
                },
                onValueChangeFinished = {
                    isDragging.value = false
                    videoPlaying = true
                    exoPlayer?.play()
                }
            )
        }
    }
}

@Composable
private fun ButtonRow(
    exoPlayer: ExoPlayer? = null,
    videoPlaying: Boolean,
    videoPlayingChange: (Boolean) -> Unit
    ) {
    Row(
        modifier = Modifier.padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Icon(
            modifier = Modifier
                .shrinkClick {
                    exoPlayer?.seekBack()
                }
                .size(40.dp),
            imageVector = Icons.Rounded.Replay5,
            contentDescription = "Replay 30",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        PlayPauseButton(
            playing = videoPlaying,
            playingStateChange = {
                videoPlayingChange(it)
                if (it) exoPlayer?.play() else exoPlayer?.pause()
            }
        )
        Icon(
            modifier = Modifier
                .shrinkClick {
                    exoPlayer?.seekForward()
                }
                .size(40.dp),
            imageVector = Icons.Rounded.Forward5,
            contentDescription = "Forward 30",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun PlayPauseButton(
    playing: Boolean,
    playingStateChange: (Boolean) -> Unit
) {
    Box {

        val transition = updateTransition(
            targetState = playing,
            label = "Play/Pause transition"
        )

        val playButtonScaleAlpha by transition.animateFloat(label = "Play Button Scale/Alpha") {
            if (!it) 1f else 0f
        }

        val pauseButtonScaleAlpha by transition.animateFloat(label = "Pause Button Scale/Alpha") {
            if (it) 1f else 0f
        }

        Icon(
            imageVector = Icons.Rounded.Pause,
            contentDescription = "Pause Video",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .noIndicationClick {
                    playingStateChange(!playing)
                }
                .size(40.dp)
                .scale(pauseButtonScaleAlpha)
                .alpha(pauseButtonScaleAlpha)
        )

        Icon(
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = "Play Video",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .noIndicationClick {
                    playingStateChange(!playing)
                }
                .size(40.dp)
                .scale(playButtonScaleAlpha)
                .alpha(playButtonScaleAlpha)
        )
    }
}

@Preview
@Composable
private fun PlayerViewLight()
{
    LurkTheme(useDarkPreviewTheme = false) {
        PlayerView()
    }
}

@Preview
@Composable
private fun PlayerViewDark()
{
    LurkTheme(useDarkPreviewTheme = true) {
        PlayerView()
    }
}