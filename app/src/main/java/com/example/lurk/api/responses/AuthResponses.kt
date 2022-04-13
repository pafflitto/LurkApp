package com.example.lurk.api.responses

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val timeTillExpiration: Long? = null,
    val scope: String? = null,
    val userless: Boolean = false
)

class UserlessTokenResponse {
    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("expires_in")
    var tokenExpireTime: Long? = null

    @SerializedName("scope")
    var scope: String? = null
}

class UserTokenResponse {
    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("expires_in")
    var tokenExpireTime: Long? = null

    @SerializedName("scope")
    var scope: String? = null

    @SerializedName("refresh_token")
    var refreshToken: String? = null
}

class UserRefreshTokenResponse {
    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("expires_in")
    var tokenExpireTime: Long? = null

    @SerializedName("scope")
    var scope: String? = null
}