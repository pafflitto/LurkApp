package com.example.lurk.datastores

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lurk.api.responses.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import java.util.*

class RedditAuthDataStore(private val context: Context) {

    private val Context.authDataStore by preferencesDataStore(name = AUTH_PREFERENCES)

    companion object {
        private const val AUTH_PREFERENCES = "auth_preferences"

        val DEVICE_UUID = stringPreferencesKey("UUID")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val TOKEN_SCOPE = stringPreferencesKey("SCOPE_TOKEN")
        val TOKEN_EXPIRE_TIME = longPreferencesKey("TOKEN_EXPIRE_TIME")
    }

    suspend fun saveAuthResponse(response: AuthResponse) {
        context.authDataStore.edit {
            response.accessToken?.let { accessToken ->
                it[ACCESS_TOKEN] = accessToken
            }
            response.refreshToken?.let { refreshToken ->
                it[REFRESH_TOKEN] = refreshToken
            }
            response.timeTillExpiration?.let { expireTime ->
                it[TOKEN_EXPIRE_TIME] = Date(Date().time + expireTime).time
            }
            response.scope?.let { scope ->
                it[TOKEN_SCOPE] = scope
            }
        }
    }

    val accessTokenFlow: StateFlow<String?> = context.authDataStore.data.mapLatest {
        Log.e("PrefManager", "Pref Changed: ${it[ACCESS_TOKEN]}")
        it[ACCESS_TOKEN]
    }.flowOn(Dispatchers.IO).stateIn(GlobalScope, SharingStarted.Eagerly, null)

    val refreshTokenFlow: StateFlow<String?> = context.authDataStore.data.mapLatest {
        it[REFRESH_TOKEN]
    }.flowOn(Dispatchers.IO).stateIn(GlobalScope, SharingStarted.Eagerly, null)

    val tokenExpireTimeFlow: StateFlow<Date> = context.authDataStore.data.mapLatest {
        val timeMilli = it[TOKEN_EXPIRE_TIME]?.toLong() ?: 0
        Date(timeMilli)
    }.flowOn(Dispatchers.IO).stateIn(GlobalScope, SharingStarted.Eagerly, Date())

    suspend fun saveUUID(uuid: String) {
        context.authDataStore.edit {
            it[DEVICE_UUID] = uuid
        }
    }

    suspend fun getUUID(): String = context.authDataStore.data.map { it[DEVICE_UUID] ?: "" }.first()
}