package com.example.lurk.screens.feed.expanded_media_screen

import com.example.lurk.screens.feed.Post
import com.google.android.exoplayer2.ExoPlayer

data class ExpandedMedia(
    val post: Post,
    val exoPlayer: ExoPlayer? = null
)
