package com.example.lurk.api.responses
import com.google.gson.annotations.SerializedName

data class SubredditSearchResponse(
    @SerializedName("subreddits")
    var subreddits: List<Subreddit> = listOf()
)