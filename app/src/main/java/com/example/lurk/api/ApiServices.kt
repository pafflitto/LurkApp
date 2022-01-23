package com.example.lurk.api

import ListingResponse
import com.example.lurk.api.responses.UserlessAuthResponse
import retrofit2.http.*

interface AuthApiService {

    @POST("access_token")
    suspend fun userlessAuthRequest(
        @HeaderMap headerMap: HashMap<String, String>,
        @Query("grant_type") grantType: String,
        @Query("device_id") deviceId: String
    ): UserlessAuthResponse
}

interface ListingApiService {
    @GET("r/{subreddit}")
    suspend fun subredditRequest(
        @HeaderMap headerMap: HashMap<String, String>,
        @Path("subreddit") subreddit: String,
        @Query("after") after: String?,
        @Query("before") before: String?,
        @Query("count") count: Int = 25
    ): ListingResponse
}