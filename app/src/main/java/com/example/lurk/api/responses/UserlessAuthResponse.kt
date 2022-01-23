package com.example.lurk.api.responses

import com.google.gson.annotations.SerializedName

/**
 * Class that is filled by retrofit in the response to a userless authentication attempt
 */
class UserlessAuthResponse {
    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("expires_in")
    var tokenExpireTime: Long? = null

    @SerializedName("scope")
    var scope: String? = null
}
