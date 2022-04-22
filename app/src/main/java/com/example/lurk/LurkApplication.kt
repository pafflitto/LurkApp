package com.example.lurk

import android.app.Application
import android.content.Context
import com.example.lurk.api.AuthManager
import com.example.lurk.datastores.RedditAuthDataStore
import com.example.lurk.datastores.UserPreferencesDataStore
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

        fun appContext(): Context {
            return instance!!.applicationContext
        }

        fun instance(): LurkApplication {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        authPrefDataStore = RedditAuthDataStore(applicationContext)
        authManager = AuthManager()

        userPrefDataStore = UserPreferencesDataStore(applicationContext)

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