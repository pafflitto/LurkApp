package com.example.lurk.datastores

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*

class RedditAuthDataStoreManager(private val context: Context) {

    private val Context.prefDataStore by preferencesDataStore(name = USER_PREFERENCES)

    companion object {
        private const val USER_PREFERENCES = "user_preferences"

        val DEVICE_UUID = stringPreferencesKey("UUID")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val TOKEN_SCOPE = stringPreferencesKey("SCOPE_TOKEN")
        val TOKEN_EXPIRE_TIME = longPreferencesKey("TOKEN_EXPIRE_TIME")

    }

    suspend fun saveUUID(uuid: String)
    {
        context.prefDataStore.edit {
            it[DEVICE_UUID] = uuid
        }
    }

    suspend fun getUUID(): String = context.prefDataStore.data.map { it[DEVICE_UUID] ?: "" }.first()


    suspend fun saveAccessToken(accessToken: String?)
    {
        accessToken ?: return
        context.prefDataStore.edit {
            it[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun getAccessToken() = context.prefDataStore.data.map { it[ACCESS_TOKEN] ?: "" }.first()

    suspend fun saveRefreshToken(refreshToken: String?)
    {
        refreshToken ?: return
        context.prefDataStore.edit {
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun saveTokenScope(scope: String?)
    {
        scope ?: return
        context.prefDataStore.edit {
            it[TOKEN_SCOPE] = scope
        }
    }

    suspend fun getTokenScope(): String = context.prefDataStore.data.map { it[TOKEN_SCOPE] ?: "" }.first()

    suspend fun getRefreshToken(): String = context.prefDataStore.data.map { it[REFRESH_TOKEN] ?: "" }.first()

    suspend fun saveTokenExpireTime(timeUntilExpiration: Long?)
    {
        timeUntilExpiration ?: return
        context.prefDataStore.edit {
            it[TOKEN_EXPIRE_TIME] = Date(Date().time + timeUntilExpiration).time
        }
    }

    suspend fun getTokenExpireTime(): Date = context.prefDataStore.data.map { Date(it[TOKEN_EXPIRE_TIME] ?: Date(0).time) }.first()
}