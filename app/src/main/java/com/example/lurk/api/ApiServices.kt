package com.example.lurk.api

import ListingResponse
import com.example.lurk.api.responses.SubredditSearchResponse
import com.example.lurk.api.responses.UserRefreshTokenResponse
import com.example.lurk.api.responses.UserTokenResponse
import com.example.lurk.api.responses.UserlessTokenResponse
import retrofit2.http.*

interface AuthApiService {

    @POST("access_token")
    suspend fun userlessTokenRequest(
        @HeaderMap headerMap: HashMap<String, String>,
        @Query("grant_type") grantType: String = RedditApiConstants.USERLESS_GRANT_TYPE_URL,
        @Query("device_id") deviceId: String
    ): UserlessTokenResponse

    @POST("access_token")
    suspend fun userTokenRequest(
        @HeaderMap headerMap: HashMap<String, String>,
        @Query("grant_type") grantType: String = RedditApiConstants.USER_GRANT_TYPE,
        @Query("code") code: String,
        @Query("redirect_uri") redirectUri: String = RedditApiConstants.REDIRECT_URL
    ): UserTokenResponse

    @POST("access_token")
    suspend fun refreshTokenRequest(
        @HeaderMap headerMap: HashMap<String, String>,
        @Query("grant_type") grantType: String = RedditApiConstants.REFRESH_GRANT_TYPE,
        @Query("refresh_token") refreshToken: String
    ): UserRefreshTokenResponse
}

interface ListingApiService {
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