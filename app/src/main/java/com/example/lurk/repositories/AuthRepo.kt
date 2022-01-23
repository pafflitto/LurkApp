package com.example.lurk.repositories

import android.util.Base64
import com.example.lurk.BuildConfig
import com.example.lurk.api.AuthResponse
import com.example.lurk.api.RedditApiConstants
import com.example.lurk.retrofit_clients.AuthClient
import kotlinx.coroutines.flow.flow

class AuthRepo {
    private val authClient = AuthClient.webservice
    private val decodedHeader = "${BuildConfig.CLIENT_ID}:"
    private val header = hashMapOf("Authorization" to "Basic ".plus(Base64.encodeToString(decodedHeader.toByteArray(), Base64.NO_WRAP)))

    suspend fun requestUserlessToken(uuid: String) = flow {
        val response = authClient.userlessAuthRequest(
            headerMap = header,
            grantType = RedditApiConstants.USERLESS_GRANT_TYPE_URL,
            deviceId = uuid
        )

        emit(
            AuthResponse(
                accessToken = response.accessToken,
                timeTillExpiration = response.tokenExpireTime,
                scope = response.scope,
                userless = true
            )
        )
    }

    suspend fun requestUserToken() {

    }
}