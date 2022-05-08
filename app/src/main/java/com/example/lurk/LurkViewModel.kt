package com.example.lurk

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lurk.repositories.FeedSource
import com.example.lurk.repositories.RedditRepo
import com.example.lurk.screens.feed.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LurkViewModel : ViewModel() {

    private val repo = RedditRepo()
    private val authManager = LurkApplication.instance().authManager

    //region Login
    fun userlessLogin() = viewModelScope.launch(Dispatchers.IO) {
        authManager.getAccess()
    }

    fun handleUserLoginResponse(code: String?, error: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (error != null) {
            // TODO Add login states and show error
        }
        else if (code != null) {
            authManager.getAccess(code)
        }
    }
    //endregion

    //region Reddit Feed
    var feedLoadingState by mutableStateOf(LoadingState.LOADED)
    val subredditFlow = MutableStateFlow("popular")
    var subredditSearchText by mutableStateOf("")
    var subredditSearchResults by mutableStateOf(emptyList<String>())

    fun subredditSelected(subreddit: String) = viewModelScope.launch(Dispatchers.IO) {
        if (subreddit != oFeed.value?.subreddit) {
            feedLoadingState = LoadingState.LOADING
            subredditFlow.value = subreddit
        }
    }

    fun subredditSearchTextChange(text: String) {
        subredditSearchText = text
        viewModelScope.launch {
            subredditSearchResults = repo.subredditSearch(subredditSearchText)
        }
    }

    val oFeed: StateFlow<Feed?> = combine(authManager.userHasAccess, subredditFlow) { hasAccess, subreddit ->
        if (hasAccess) {
            feedLoadingState = LoadingState.LOADED
            val postsFlow = Pager(PagingConfig(pageSize = 100)) {
                FeedSource(
                    subreddit = subreddit,
                    repo = repo
                )
            }.flow.cachedIn(viewModelScope)
            Feed(subreddit = subreddit.toTitleCase(), postFlow = postsFlow)
        }
        else {
            // TODO add in error loading state here and handle with responsibility!
            null
        }
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun voteStatusUpdated(vote: Post.Companion.Voted) {

    }
    //endregion

    //region Account
    fun toggleFavoriteSubreddit(subreddit: String, currentlyFavorited: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPrefDataStore.saveRemoveFavoriteSubreddit(subreddit = subreddit.lowercase(), shouldSave = !currentlyFavorited)
        }
    }

    // TODO Need to change this trigger of subredditFlow!!!!!
    val oUserSubreddits: StateFlow<Map<String, List<UserSubreddit>>> = subredditFlow.mapLatest { subreddit ->
        repo.getUserSubreddits() ?: emptyMap()
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())
    //endregion

    //region User Settings
    fun setUserTheme(theme: UserTheme) = viewModelScope.launch(Dispatchers.IO) {
        userPrefDataStore.saveTheme(theme)
    }
    //endregion
}

data class Feed(
    val subreddit: String,
    val postFlow: Flow<PagingData<Post>>,
    val sortingType: SortingType = SortingType.BEST
)

enum class UserTheme(val displayText: String, val icon: ImageVector) {
    Auto("Auto", Icons.Rounded.Schedule),
    Light("Light", Icons.Rounded.LightMode),
    Dark("Dark", Icons.Rounded.DarkMode),
    MaterialYou("Material You", Icons.Rounded.AutoAwesome)
}

data class UserSubreddit(val name: String, val favorited: Boolean? = null)

enum class SortingType {
    BEST,
    POPULAR,
    NEW,
    CONTROVERSIAL
}

enum class LoadingState {
    LOADING,
    LOADED,
    ERROR
}