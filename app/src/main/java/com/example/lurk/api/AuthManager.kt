package com.example.lurk.api

import com.example.lurk.LurkApplication
import com.example.lurk.authDataStore
import com.example.lurk.repositories.AuthRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

@OptIn(DelicateCoroutinesApi::class)
class AuthManager: CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.IO) {

    private val repo = AuthRepo()

    fun getAccess(bearerToken: String? = null) = launch(Dispatchers.IO) {
        when {
            authDataStore.refreshTokenFlow.value?.isNotBlank() == true -> {
                // User previously signed in
                authDataStore.refreshTokenFlow.value?.let { refreshToken ->
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val userHasAccess: StateFlow<Boolean> = combine(authDataStore.accessTokenFlow, authDataStore.refreshTokenFlow, authDataStore.tokenExpireTimeFlow) { accessToken, refreshToken, expireDate ->
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