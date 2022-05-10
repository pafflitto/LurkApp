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

    /**
     * Function that requests an access token from Reddit. It will use a refresh token if one
     * is available. The bearer token is not null if the user is signing into reddit with their account.
     * If the user is trying to gain userless access then we create a UUID and request a token with that.
     *
     * @param bearerToken String containing the response from the user signing into to reddit
     */
    fun getAccess(bearerToken: String? = null, refreshToken: String? = null) = launch(Dispatchers.IO) {
        when {
            authDataStore.oRefreshToken.value?.isNotBlank() == true -> {
                // User previously signed in
                refreshToken?.let {
                    repo.refreshToken(it)
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

    /**
     * Hot Flow containing the state of the user's access to the reddit api. Will get triggered on the access token, refresh toke, and a change in the token expire time
     * If the expire time is reached, we call getAccess to either use the refresh token or generate a new userless token
     */
    val userHasAccess: StateFlow<Boolean> = combine(authDataStore.oAccessToken, authDataStore.oRefreshToken, authDataStore.oTokenExpireTime) { accessToken, refreshToken, expireDate ->
        accessToken?.let {
            if (Date().after(expireDate)) {
                // fetch new token with refresh token or get another userless token
                getAccess(refreshToken = refreshToken)
                false
            }
            else {
                true
            }
        } ?: false
    }.flowOn(Dispatchers.IO).stateIn(this, SharingStarted.Eagerly, false)
}