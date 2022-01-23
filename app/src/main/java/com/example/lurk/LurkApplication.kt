package com.example.lurk

import android.app.Application
import android.content.Context
import com.example.lurk.api.AuthManager
import com.example.lurk.datastores.RedditAuthDataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LurkApplication : Application() {

    val prefManager: RedditAuthDataStoreManager
    val authManager: AuthManager

    init {
        instance = this
        prefManager = RedditAuthDataStoreManager(this)
        authManager = AuthManager(prefManager)
    }

    companion object {
        private var instance: LurkApplication? = null

        fun appContext(): Context {
            return instance!!.applicationContext
        }

        fun instance(): LurkApplication {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch()
        {
            val uuid = prefManager.getUUID()
            if (uuid.isBlank())
            {
                prefManager.saveUUID(UUID.randomUUID().toString())
            }
        }
    }
}