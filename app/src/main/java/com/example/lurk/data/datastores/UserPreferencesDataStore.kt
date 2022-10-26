package com.example.lurk.data.datastores

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lurk.screens.settings.UserTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * Datastore to hold the User's selected preferences.
 * For example, the theme and their favorite subreddits
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferencesDataStore(
    private val context: Context
) {

    private val Context.userPrefDataStore by preferencesDataStore(name = USER_PREFS)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object {
        private const val USER_PREFS = "USER_PREFS"

        val THEME = intPreferencesKey("THEME")
        val FAVORITE_SUBREDDITS = stringPreferencesKey("FAV_SUBREDDITS")
    }

    /**
     * Saves the user selected theme to the datastore
     */
    suspend fun saveTheme(theme: UserTheme) {
        context.userPrefDataStore.edit {
            it[THEME] = theme.ordinal
        }
    }

    val userThemeFlow: StateFlow<UserTheme> = context.userPrefDataStore.data.mapLatest {
        UserTheme.values()[it[THEME] ?: 0]
    }.flowOn(Dispatchers.IO).stateIn(coroutineScope, SharingStarted.Eagerly, UserTheme.Auto)

    /**
     * Function to save or remove a favorite subreddit from the user's lists of subreddits
     *
     * @param subreddit string of subreddit name to save or remove from the fav list
     * @param shouldSave boolean telling the function to save of remove the subreddit from the datastore
     */
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

    // Flow of the user's favorite subreddits. Subreddit names are seperated by a comma. For example, android, androidDev, programminghumor
    val favoriteSubredditFlow: StateFlow<List<String>> = context.userPrefDataStore.data.mapLatest {
        it[FAVORITE_SUBREDDITS]?.split(",") ?: emptyList()
    }.flowOn(Dispatchers.IO).stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())
}