package com.example.lurk.api

import ListingResponse
import com.example.lurk.api.responses.SubredditSearchResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApi {
    @GET("r/{subreddit}")
    suspend fun subredditRequest(
        @HeaderMap headerMap: HashMap<String, String>,
        @Path("subreddit") subreddit: String,
        @Query("after") after: String?,
        @Query("before") before: String?,
        @Query("count") count: Int = 100
    ): ListingResponse

    @GET("subreddits/mine/subscriber")
    suspend fun userSubreddits(
        @HeaderMap headerMap: HashMap<String, String>,
    ): ListingResponse

    @GET("subreddits/default")
    suspend fun userlessSubreddits(
        @HeaderMap headerMap: HashMap<String, String>
    ): ListingResponse

    @GET("api/subreddit_autocomplete")
    suspend fun subredditSearch(
        @HeaderMap headerMap: HashMap<String, String>,
        @Query("query") searchQuery: String,
        @Query("include_over_18") includeNSFW: Boolean = false,
        @Query("include_profiles") includeProfiles: Boolean = false
    ): SubredditSearchResponse
}