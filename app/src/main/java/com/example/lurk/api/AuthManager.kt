package com.example.lurk.api

import com.example.lurk.LurkApplication
import com.example.lurk.datastores.RedditAuthDataStoreManager
import com.example.lurk.repositories.AuthRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.*

class AuthManager(
    private val prefManager: RedditAuthDataStoreManager
    ) {

    private val repo = AuthRepo()

    suspend fun getAccess(): Boolean {
        var grantAccess = false

        if (prefManager.getRefreshToken().isNotBlank())
        {
            // User previously signed in
        }
        else
        {
            // Userless
            val uuid = LurkApplication.instance().prefManager.getUUID()

            repo.requestUserlessToken(uuid).collect {
                // Save token information to our data store
                prefManager.saveAccessToken(it.accessToken)
                prefManager.saveRefreshToken(it.refreshToken)
                prefManager.saveTokenExpireTime(it.timeTillExpiration)
                prefManager.saveTokenScope(it.scope)

                grantAccess =  it.accessToken?.isNotBlank() ?: false
            }
        }

        return grantAccess
    }

    val accessTokenValid = flow {
        val accessToken = prefManager.getAccessToken()
        val expired = Date().after(prefManager.getTokenExpireTime())

        when {
            accessToken.isNotBlank() && expired -> {
                emit(getAccess())
            }
            accessToken.isBlank() -> {
                emit(false)
            }
            else -> {
                emit(true)
            }
        }
    }


}