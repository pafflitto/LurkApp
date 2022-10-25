package com.example.lurk.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.delay

@Composable
fun ExoPlayer?.MonitorProgressUpdates(
    progress: MutableState<Float>,
    isDragging: MutableState<Boolean>
) {
    this?.let {
        LaunchedEffect(Unit) {
            while (true) {
                if (playbackState == Player.STATE_READY && !isDragging.value) {
                    var loopDelay: Float = 20 - (progress.value % 20)
                    if (loopDelay < 0) {
                        loopDelay += 20
                    }

                    progress.value = currentPosition / duration.toFloat()
                    delay(loopDelay.toLong())
                } else delay(500)
            }
        }
    }
}