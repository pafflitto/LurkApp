package com.example.lurk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.lurk.repositories.FeedSource

class FeedViewModel : ViewModel() {
    var subreddit: String = "popular"
        set(newSub) {
            field = newSub
            source = FeedSource(newSub)
        }

    private var source = FeedSource(subreddit)

    val posts = Pager(PagingConfig(pageSize = 25)) { source }.flow.cachedIn(viewModelScope)


    companion object {
        enum class SortingType {
            BEST,
            POPULAR,
            NEW,
            CONTROVERSIAL
        }
    }
}