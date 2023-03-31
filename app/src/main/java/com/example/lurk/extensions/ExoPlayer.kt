package com.example.lurk.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import com.google.android.exoplayer2.C.TRACK_TYPE_AUDIO
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExoPlayer?.MonitorProgressUpdates(
    progress: MutableState<Float>,
    isDragging: MutableState<Boolean>
) {
    val scope = rememberCoroutineScope()
    this?.let {
        DisposableEffect(Unit) {
            scope.launch {
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
            onDispose {
                release()
            }
        }
    }
}

val ExoPlayer?.isMuted: Boolean get() = this?.volume?.equals(0f) ?: false

val ExoPlayer.hasAudio: Boolean get() = this.currentTracks.containsType(TRACK_TYPE_AUDIO)
