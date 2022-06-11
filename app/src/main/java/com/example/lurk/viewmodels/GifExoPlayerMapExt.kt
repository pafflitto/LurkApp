package com.example.lurk.viewmodels

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.lurk.LurkApplication
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private fun getDefaultPlayer(url: String) = ExoPlayer.Builder(LurkApplication.appContext).build().apply {
    playWhenReady = false
    repeatMode = Player.REPEAT_MODE_ALL
    addMediaItem(MediaItem.fromUri(url))
    prepare()
}

suspend fun SnapshotStateMap<String, ExoPlayer>.updateVisibleItems(items: Map<Pair<String, Boolean>, String>) {
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