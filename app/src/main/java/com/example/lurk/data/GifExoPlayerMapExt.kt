package com.example.lurk.data

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun SnapshotStateMap<String, ExoPlayer>.updateVisibleItems(
    getDefaultPlayer: (url: String) -> ExoPlayer,
    items: Map<Pair<String, Boolean>, String>
) {
    keys.forEach { activeIndex ->
        if (!items.keys.map { it.first }.contains(activeIndex)) {
            val player = this[activeIndex]
            withContext(Dispatchers.Main) {
                player?.release()
            }
            keys.remove(activeIndex)
        }
    }

    items.forEach { entry ->
        val key = entry.key.first
        val shouldPlay = entry.key.second

        if (!containsKey(key)) {
            withContext(Dispatchers.Main) {
                this@updateVisibleItems[key] = getDefaultPlayer(entry.value)
            }
        }

        // In center of screen? then play gif
        if (containsKey(key) && shouldPlay) {
            withContext(Dispatchers.Main) {
                this@updateVisibleItems[entry.key.first]?.play()
            }
        }
        else if (containsKey(key) && !shouldPlay) {
            withContext(Dispatchers.Main) {
                this@updateVisibleItems[entry.key.first]?.pause()
            }
        }
    }
}