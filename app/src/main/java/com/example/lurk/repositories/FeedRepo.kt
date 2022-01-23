package com.example.lurk.repositories

import ListingResponse
import com.example.lurk.LurkApplication
import com.example.lurk.api.ListingClient
import kotlinx.coroutines.flow.collect

class FeedRepo {

    private val authManager = LurkApplication.instance().authManager
    private val prefManager = LurkApplication.instance().prefManager
    private val listingClient = ListingClient.webservice

    suspend fun requestSubreddit(
        subreddit: String?,
        after: String?,
        before: String?
    ): ListingResponse? {
        val haveActiveToken = checkAccessToken()
        if (!haveActiveToken) return null

        return listingClient.subredditRequest(getAuthHeader(), subreddit ?: "popular", after, before)

    }

    private suspend fun checkAccessToken(): Boolean
    {
        var valid = false
        authManager.accessTokenValid.collect { tokenValid ->
            valid = tokenValid
        }

        if (!valid) {
            // Could not get new token or use refresh token
            // most likely no internet
            // do something here
        }

        return valid
    }

    private suspend fun getAuthHeader(): HashMap<String, String> {
        return hashMapOf(
            "User-Agent" to "Lurk for Reddit",
            "Authorization" to "Bearer ".plus(prefManager.getAccessToken())
        )
    }
}