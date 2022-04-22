package com.example.lurk.datastores

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lurk.viewmodels.UserTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*

class UserPreferencesDataStore(private val context: Context) {

    private val Context.userPrefDataStore by preferencesDataStore(name = USER_PREFS)

    companion object {
        private const val USER_PREFS = "USER_PREFS"

        val THEME = intPreferencesKey("THEME")
    }

    suspend fun saveTheme(theme: UserTheme) {
        context.userPrefDataStore.edit {
            it[THEME] = theme.ordinal
        }
    }

    val userThemeFlow: StateFlow<UserTheme> = context.userPrefDataStore.data.mapLatest {
        UserTheme.values()[it[THEME] ?: 0]
    }.flowOn(Dispatchers.IO).stateIn(GlobalScope, SharingStarted.Eagerly, UserTheme.Auto)
}