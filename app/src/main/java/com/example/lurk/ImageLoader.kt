package com.example.lurk

import android.content.Context
import android.graphics.drawable.Drawable
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext

interface RedditImageLoader {
    suspend fun loadImage(url: String): Drawable?
}

class RedditImageLoaderImpl(
    @ApplicationContext private val context: Context
): RedditImageLoader {
    private val loader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.2)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.1)
                .build()
        }
        .build()

    override suspend fun loadImage(url: String) = loader
        .execute(
            ImageRequest.Builder(context)
                .data(url)
                .build()
        ).drawable

}