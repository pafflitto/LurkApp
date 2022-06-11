package com.example.lurk.viewmodels

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.DrawerValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lurk.LurkApplication
import com.example.lurk.repositories.FeedSource
import com.example.lurk.repositories.RedditRepo
import com.example.lurk.screens.feed.Post
import com.example.lurk.toTitleCase
import com.example.lurk.userPrefDataStore
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class FeedViewModel: ViewModel() {
    private val repo = RedditRepo()
    private val authManager = LurkApplication.instance().authManager
    private val mutex = Mutex()

    val feedListState = LazyListState()
    var feedLoadingState by mutableStateOf(LoadingState.LOADED)
    val subredditFlow = MutableStateFlow("Popular")
    val gifExoPlayers = mutableStateMapOf<String, ExoPlayer>()

    val oFeed: StateFlow<Feed?> = combine(authManager.userHasAccess, subredditFlow) { hasAccess, subreddit ->
        if (hasAccess) {
            withContext(Dispatchers.Main) {
                feedLoadingState = LoadingState.LOADED
            }
            val postsFlow = Pager(PagingConfig(pageSize = 50)) {
                FeedSource(
                    subreddit = subreddit,
                    repo = repo,
                    coroutineScope = viewModelScope
                )
            }.flow.cachedIn(viewModelScope)
            Feed(subreddit = subreddit.toTitleCase(), postFlow = postsFlow)
        }
        else {
            // TODO add in error loading state here and handle with responsibility!
            null
        }
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun updateVisibleItems(posts: Map<Pair<String, Boolean>, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (posts.isNotEmpty()) {
                mutex.withLock {
                    gifExoPlayers.updateVisibleItems(posts)
                }
            }
        }
    }

    fun voteStatusUpdated(vote: Post.Companion.Voted) {

    }

    //region Subreddit Selector
    var subredditSearchText by mutableStateOf("")
    var subredditSearchResults by mutableStateOf(emptyList<String>())
    private val _drawerState = MutableStateFlow(DrawerValue.Closed)

    fun clearSubredditSearchText() {
        subredditSearchText = ""
        subredditSearchResults = emptyList()
    }

    fun updateDrawerState(stateValue: DrawerValue) = viewModelScope.launch(Dispatchers.IO) {
        _drawerState.value = stateValue
    }

    fun subredditSelected(subreddit: String) = viewModelScope.launch(Dispatchers.IO) {
        if (subreddit != oFeed.value?.subreddit) {
            feedLoadingState = LoadingState.LOADING
            subredditFlow.value = subreddit
            withContext(Dispatchers.Main) {
                feedListState.scrollToItem(0)
            }
        }
    }

    fun toggleFavoriteSubreddit(subreddit: String, currentlyFavorited: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPrefDataStore.saveRemoveFavoriteSubreddit(subreddit = subreddit.lowercase(), shouldSave = !currentlyFavorited)
        }
    }

    val oUserSubreddits: StateFlow<Map<String, List<UserSubreddit>>> = _drawerState.mapLatest {
        repo.getUserSubreddits() ?: emptyMap()
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    fun subredditSearchTextChange(text: String) {
        subredditSearchText = text
        viewModelScope.launch {
            subredditSearchResults = repo.subredditSearch(subredditSearchText)
        }
    }

    data class Feed(
        val subreddit: String,
        val postFlow: Flow<PagingData<Post>>,
        val sortingType: SortingType = SortingType.BEST
    )
    //endregion
}