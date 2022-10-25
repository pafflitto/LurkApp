package com.example.lurk.repositories

import android.util.Base64
import com.example.lurk.BuildConfig
import com.example.lurk.api.RedditAuthApi
import com.example.lurk.api.responses.AuthResponse
import com.example.lurk.datastores.RedditAuthDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

interface RedditAuthRepo : Repo {
    suspend fun requestUserlessToken(): Boolean
    suspend fun requestUserToken(code: String)
    suspend fun refreshToken(): Boolean
    fun userHasAccess(scope: CoroutineScope): StateFlow<Boolean>
    suspend fun getUserAccountType(): AccountType
    suspend fun getAccessToken(): String?
}

class RedditAuthRepoImpl(
    private val authApi: RedditAuthApi,
    private val authDataStore: RedditAuthDataStore
): RedditAuthRepo {

    private val decodedHeader = "${BuildConfig.CLIENT_ID}:"
    private val header = hashMapOf("Authorization" to "Basic ".plus(Base64.encodeToString(decodedHeader.toByteArray(), Base64.NO_WRAP)))

    override fun userHasAccess(scope: CoroutineScope): StateFlow<Boolean> = authDataStore.oAccessToken.map {
        it != null
    }.stateIn(scope, SharingStarted.Eagerly, false)

    override suspend fun requestUserlessToken(): Boolean {
        val uuid = authDataStore.getUUID()
        val result = apiCall(this) {
            authApi.userlessTokenRequest(
                headerMap = header,
                deviceId = uuid
            ).let { apiResponse ->
                AuthResponse(
                    accessToken = apiResponse.accessToken,
                    scope = apiResponse.scope,
                    timeTillExpiration = apiResponse.tokenExpireTime
                )
            }
        }.onSuccess {
            authDataStore.saveAuthResponse(response = it, userlessLogin = true)
        }

        return result.isSuccess
    }

    override suspend fun requestUserToken(code: String) {
        apiCall(this) {
            authApi.userTokenRequest(
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
        }.onSuccess {
            authDataStore.saveAuthResponse(response = it, userlessLogin = false)
        }
    }

    override suspend fun refreshToken(): Boolean {
        val noRefreshTokenError = Throwable("No Refresh Token found!")

        val result = apiCall(this) {

            val refreshToken = authDataStore.oRefreshToken.value ?: throw noRefreshTokenError

            authApi.refreshTokenRequest(
                headerMap = header,
                refreshToken = refreshToken
            ).let { apiResponse ->
                AuthResponse(
                    accessToken = apiResponse.accessToken,
                    scope = apiResponse.scope,
                    timeTillExpiration = apiResponse.tokenExpireTime,
                )
            }
        }.onSuccess {
            authDataStore.saveAuthResponse(response = it)
        }

        return result.isSuccess
    }

    override suspend fun getUserAccountType(): AccountType = if (authDataStore.oUserSignedIn.value) {
        AccountType.SignedInUser
    } else AccountType.Userless

    override suspend fun getAccessToken(): String? = authDataStore.oAccessToken.value
}

sealed class AccountType {
    object Userless: AccountType()
    object SignedInUser: AccountType()
}