package com.example.lurk.screens.feed

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lurk.data.repositories.Feed
import com.example.lurk.data.repositories.RedditRepo
import com.example.lurk.data.updateVisibleItems
import com.example.lurk.extensions.toTitleCase
import com.example.lurk.screens.feed.postviews.Post
import com.example.lurk.screens.login.UserSubreddit
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val redditRepo: RedditRepo,
    @ApplicationContext context: Context
): AndroidViewModel(context as Application) {
    private val mutex = Mutex()
    val gifExoPlayers = mutableStateMapOf<String, ExoPlayer>()

    var subredditSearchText by mutableStateOf("")
    var subredditSearchResults by mutableStateOf(emptyList<String>())

    private val _feedState = MutableStateFlow<FeedState>(FeedState.Loading(false))
    val feedStateFlow: StateFlow<FeedState> = _feedState

    private val _userSubreddits = MutableStateFlow<Map<String, List<UserSubreddit>>>(emptyMap())
    val userSubredditsFlow: StateFlow<Map<String, List<UserSubreddit>>> = _userSubreddits

    private val _currentSubreddit = MutableStateFlow("Popular")
    val currentSubredditFlow: StateFlow<String> = _currentSubreddit

    private fun buildExoPlayer(url: String) = ExoPlayer.Builder(getApplication())
        .setSeekForwardIncrementMs(5000)
        .setSeekBackIncrementMs(5000)
        .build().apply {
            playWhenReady = false
            repeatMode = Player.REPEAT_MODE_ALL
            addMediaItem(MediaItem.fromUri(url))
            prepare()
        }

    init {
        updateSubreddit("popular")
        updateSubreddits()
    }

    fun updateSubreddit(subreddit: String, refresh: Boolean = false) = viewModelScope.launch {
        _currentSubreddit.tryEmit(subreddit.toTitleCase())
        _feedState.tryEmit(FeedState.Loading(refresh))
        redditRepo.getSubreddit(
            subreddit = subreddit,
            scope = viewModelScope
        ).onSuccess {
            _feedState.tryEmit(
                FeedState.Loaded(
                    fromRefresh = refresh,
                    feed = it
                )
            )
        }
    }

    fun updateVisibleItems(posts: Map<Pair<String, Boolean>, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (posts.isNotEmpty()) {
                mutex.withLock {
                    gifExoPlayers.updateVisibleItems(
                        getDefaultPlayer = this@FeedViewModel::buildExoPlayer,
                        items = posts
                    )
                }
            }
        }
    }

    fun updateSubreddits() = viewModelScope.launch {
        redditRepo.userSubreddits()
            .onSuccess {
                _userSubreddits.tryEmit(it)
            }
    }

    fun refreshFeed() = updateSubreddit(_currentSubreddit.value, true)

    fun searchForSubreddit(query: String) = viewModelScope.launch {
        subredditSearchText = query
        redditRepo.subredditSearch(subredditSearchText).onSuccess {
            subredditSearchResults = it
        }
    }

    fun toggleFavoriteSubreddit(subreddit: String, currentlyFavorited: Boolean) = viewModelScope.launch {
        redditRepo.toggleFavoriteSubreddit(subreddit, !currentlyFavorited)
    }

    fun clearSubredditSearchText() {
        subredditSearchText = ""
        subredditSearchResults = emptyList()
    }

    fun voteStatusUpdated(vote: Post.Companion.Voted) {

    }

    //endregion
}

sealed class FeedState {
    data class Loading(
        val refresh: Boolean
    ) : FeedState()
    object Error : FeedState()
    data class Loaded(
        val fromRefresh: Boolean,
        val feed : Feed
    ) : FeedState()
}
