package com.example.lurk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.lurk.repositories.FeedSource
import com.example.lurk.screens.feed.Post

class FeedViewModel : ViewModel() {
    var subreddit: String = "popular"
        set(newSub) {
            source = FeedSource(newSub)
            field = newSub
        }

    private var source = FeedSource(subreddit)

    val posts = Pager(PagingConfig(pageSize = 100)) { source }.flow.cachedIn(viewModelScope)

    fun voteStatusUpdated(vote: Post.Companion.Voted) {

    }

    companion object {
        enum class SortingType {
            BEST,
            POPULAR,
            NEW,
            CONTROVERSIAL
        }
    }
}