package com.example.lurk.repositories

import android.util.Base64
import com.example.lurk.BuildConfig
import com.example.lurk.api.responses.AuthResponse
import com.example.lurk.authDataStore
import com.example.lurk.retrofit_clients.AuthClient

class AuthRepo {
    private val authClient = AuthClient.webservice
    private val decodedHeader = "${BuildConfig.CLIENT_ID}:"
    private val header = hashMapOf("Authorization" to "Basic ".plus(Base64.encodeToString(decodedHeader.toByteArray(), Base64.NO_WRAP)))

    suspend fun requestUserlessToken(uuid: String) {
        val response = authClient.userlessTokenRequest(
            headerMap = header,
            deviceId = uuid
        ).let { apiResponse ->
            AuthResponse(
                accessToken = apiResponse.accessToken,
                scope = apiResponse.scope,
                timeTillExpiration = apiResponse.tokenExpireTime
            )
        }

        authDataStore.saveAuthResponse(response)
    }

    suspend fun requestUserToken(code: String) {
        val response = authClient.userTokenRequest(
            headerMap = header,
            code = code,
        ).let { apiResponse ->
            AuthResponse(
                accessToken = apiResponse.accessToken,
                refreshToken = apiResponse.refreshToken,
                scope = apiResponse.scope,
                timeTillExpiration = apiResponse.tokenExpireTime
            )
        }

        authDataStore.saveAuthResponse(response)
    }

    suspend fun refreshToken(refreshToken: String) {
        val response = authClient.refreshTokenRequest(
            headerMap =  header,
            refreshToken = refreshToken
        ).let { apiResponse ->
            AuthResponse(
                accessToken = apiResponse.accessToken,
                scope = apiResponse.scope,
                timeTillExpiration = apiResponse.tokenExpireTime,
            )
        }

        authDataStore.saveAuthResponse(response)
    }
}