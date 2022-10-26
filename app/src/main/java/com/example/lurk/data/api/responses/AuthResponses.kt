package com.example.lurk.data.api.responses

import com.google.gson.annotations.SerializedName

// Response that is used to be saved into the datastore
data class AuthResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val timeTillExpiration: Long? = null,
    val scope: String? = null,
    val userless: Boolean = false
)

// Response from the server that fills the AuthResponse above then saved to the data store
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

// Response from the server for a signed in user that fills the AuthRespone above then saved to the data store
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