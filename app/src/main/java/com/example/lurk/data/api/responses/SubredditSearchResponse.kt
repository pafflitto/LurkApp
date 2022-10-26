package com.example.lurk.data.api.responses
import com.google.gson.annotations.SerializedName

// Class holding the list of subreddits that are returned from a search request
data class SubredditSearchResponse(
    @SerializedName("subreddits")
    var subreddits: List<Subreddit> = listOf()
)