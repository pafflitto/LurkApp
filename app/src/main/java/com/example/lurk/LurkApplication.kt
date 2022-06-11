package com.example.lurk

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.lurk.api.AuthManager
import com.example.lurk.datastores.RedditAuthDataStore
import com.example.lurk.datastores.UserPreferencesDataStore
import com.gfycat.core.GfyCoreInitializationBuilder
import com.gfycat.core.GfyCoreInitializer
import com.gfycat.core.GfycatApplicationInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LurkApplication : Application() {

    lateinit var authPrefDataStore: RedditAuthDataStore
        private set

    lateinit var authManager: AuthManager
        private set

    lateinit var userPrefDataStore: UserPreferencesDataStore
        private set

    init {
        instance = this
    }

    companion object {
        private var instance: LurkApplication? = null

        val appContext: Context get() = instance!!.applicationContext

        fun instance(): LurkApplication {
            return instance!!
        }

        val imageLoader: ImageLoader by lazy {
            ImageLoader.Builder(appContext)
                .memoryCache {
                    MemoryCache.Builder(appContext)
                        .maxSizePercent(0.2)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(appContext.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.1)
                        .build()
                }
                .build()
        }

        val videoFrameLoader: ImageLoader by lazy {
            ImageLoader.Builder(appContext)
                .components {
                    add(VideoFrameDecoder.Factory())
                }
                .memoryCache {
                    MemoryCache.Builder(appContext)
                        .maxSizePercent(0.2)
                        .build()
                }.build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        authPrefDataStore = RedditAuthDataStore(applicationContext)
        authManager = AuthManager()
        userPrefDataStore = UserPreferencesDataStore(applicationContext)

        GfyCoreInitializer.initialize(
            GfyCoreInitializationBuilder(applicationContext, GfycatApplicationInfo(BuildConfig.GFYCAT_CLIENT_ID, BuildConfig.GFYCAT_CLIENT_SECRET))
        )

        CoroutineScope(Dispatchers.IO).launch()
        {
            val uuid = authPrefDataStore.getUUID()
            if (uuid.isBlank())
            {
                authPrefDataStore.saveUUID(UUID.randomUUID().toString())
            }
        }
    }
}