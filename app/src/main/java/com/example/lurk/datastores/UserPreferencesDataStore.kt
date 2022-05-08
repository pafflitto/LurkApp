package com.example.lurk.datastores

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lurk.UserTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*

class UserPreferencesDataStore(private val context: Context) {

    private val Context.userPrefDataStore by preferencesDataStore(name = USER_PREFS)

    companion object {
        private const val USER_PREFS = "USER_PREFS"

        val THEME = intPreferencesKey("THEME")
        val FAVORITE_SUBREDDITS = stringPreferencesKey("FAV_SUBREDDITS")
    }

    suspend fun saveTheme(theme: UserTheme) {
        context.userPrefDataStore.edit {
            it[THEME] = theme.ordinal
        }
    }

    val userThemeFlow: StateFlow<UserTheme> = context.userPrefDataStore.data.mapLatest {
        UserTheme.values()[it[THEME] ?: 0]
    }.flowOn(Dispatchers.IO).stateIn(GlobalScope, SharingStarted.Eagerly, UserTheme.Auto)

    suspend fun saveRemoveFavoriteSubreddit(subreddit: String, shouldSave: Boolean) {
        context.userPrefDataStore.edit { prefs ->
            if (shouldSave) {
                prefs[FAVORITE_SUBREDDITS] = buildString {
                    append(favoriteSubredditFlow.value.joinToString(","))
                    append("$subreddit,")
                }
            }
            else {
                prefs[FAVORITE_SUBREDDITS] = favoriteSubredditFlow.value.toMutableList().let { favList ->
                    favList.remove(subreddit)
                    favList.joinToString(",")
                }
            }
        }
    }

    val favoriteSubredditFlow: StateFlow<List<String>> = context.userPrefDataStore.data.mapLatest {
        it[FAVORITE_SUBREDDITS]?.split(",") ?: emptyList()
    }.flowOn(Dispatchers.IO).stateIn(GlobalScope, SharingStarted.Eagerly, emptyList())
}