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

    lateinit var authPrefManager: RedditAuthDataStoreManager
        private set

    lateinit var authManager: AuthManager
        private set

    init {
        instance = this
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
        authPrefManager = RedditAuthDataStoreManager(applicationContext)
        authManager = AuthManager()

        CoroutineScope(Dispatchers.IO).launch()
        {
            val uuid = authPrefManager.getUUID()
            if (uuid.isBlank())
            {
                authPrefManager.saveUUID(UUID.randomUUID().toString())
            }
        }
    }
}