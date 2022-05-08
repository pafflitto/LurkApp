package com.example.lurk.datastores

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    private val coroutineScope = GlobalScope

    companion object {
        private const val AUTH_PREFERENCES = "auth_preferences"

        val DEVICE_UUID = stringPreferencesKey("UUID")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val TOKEN_SCOPE = stringPreferencesKey("SCOPE_TOKEN")
        val TOKEN_EXPIRE_TIME = longPreferencesKey("TOKEN_EXPIRE_TIME")

        val USER_SIGNED_IN = booleanPreferencesKey("USER_SIGNED_IN")
    }

    suspend fun saveAuthResponse(response: AuthResponse, userlessLogin: Boolean = oUserSignedIn.value) {
        context.authDataStore.edit {
            response.accessToken?.let { accessToken ->
                it[ACCESS_TOKEN] = accessToken
                
                it[USER_SIGNED_IN] = !userlessLogin
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

    val oAccessToken: StateFlow<String?> = context.authDataStore.data.mapLatest {
        it[ACCESS_TOKEN]
    }.flowOn(Dispatchers.IO).stateIn(coroutineScope, SharingStarted.Eagerly, null)

    val oRefreshToken: StateFlow<String?> = context.authDataStore.data.mapLatest {
        it[REFRESH_TOKEN]
    }.flowOn(Dispatchers.IO).stateIn(coroutineScope, SharingStarted.Eagerly, null)

    val oTokenExpireTime: StateFlow<Date> = context.authDataStore.data.mapLatest {
        val timeMilli = it[TOKEN_EXPIRE_TIME]?.toLong() ?: 0
        Date(timeMilli)
    }.flowOn(Dispatchers.IO).stateIn(coroutineScope, SharingStarted.Eagerly, Date())

    val oUserSignedIn: StateFlow<Boolean> = context.authDataStore.data.mapLatest {
        it[USER_SIGNED_IN] ?: false
    }.flowOn(Dispatchers.IO).stateIn(coroutineScope, SharingStarted.Eagerly, false)

    suspend fun saveUUID(uuid: String) {
        context.authDataStore.edit {
            it[DEVICE_UUID] = uuid
        }
    }

    suspend fun getUUID(): String = context.authDataStore.data.map { it[DEVICE_UUID] ?: "" }.first()
}