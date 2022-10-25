package com.example.lurk.api

import com.example.lurk.api.responses.UserRefreshTokenResponse
import com.example.lurk.api.responses.UserTokenResponse
import com.example.lurk.api.responses.UserlessTokenResponse
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query

interface RedditAuthApi {

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
