package com.example.lurk.api

import com.example.lurk.LurkApplication
import com.example.lurk.authDataStore
import com.example.lurk.repositories.RedditAuthRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class AuthManager: CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.IO) {

    private val repo = RedditAuthRepo()

    fun getAccess(bearerToken: String? = null) = launch(Dispatchers.IO) {
        when {
            authDataStore.oRefreshToken.value?.isNotBlank() == true -> {
                // User previously signed in
                authDataStore.oRefreshToken.value?.let { refreshToken ->
                    repo.refreshToken(refreshToken)
                }
            }
            bearerToken?.isNotBlank() == true -> {
                // User login
                repo.requestUserToken(bearerToken)
            }
            else -> {
                // Userless
                val uuid = LurkApplication.instance().authPrefDataStore.getUUID()
                repo.requestUserlessToken(uuid)
            }
        }
    }

    val userHasAccess: StateFlow<Boolean> = combine(authDataStore.oAccessToken, authDataStore.oRefreshToken, authDataStore.oTokenExpireTime) { accessToken, refreshToken, expireDate ->
        accessToken?.let {
            if (Date().after(expireDate)) {
                // fetch new token with refresh token or get another userless token
                getAccess()
                false
            }
            else {
                true
            }
        } ?: false
    }.flowOn(Dispatchers.IO).stateIn(this, SharingStarted.Eagerly, false)
}