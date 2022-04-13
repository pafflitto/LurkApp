package com.example.lurk.repositories

import ListingResponse
import com.example.lurk.LurkApplication
import com.example.lurk.api.ListingClient

class FeedRepo {

    private val authManager = LurkApplication.instance().authManager
    private val prefManager = LurkApplication.instance().authPrefManager
    private val listingClient = ListingClient.webservice

    suspend fun requestSubreddit(
        subreddit: String?,
        after: String?,
        before: String?
    ): ListingResponse? {
        if (!authManager.userHasAccess.value) return null

        return listingClient.subredditRequest(getAuthHeader(), subreddit ?: "popular", after, before)

    }

    private fun getAuthHeader(): HashMap<String, String> {
        return hashMapOf(
            "User-Agent" to "Lurk for Reddit",
            "Authorization" to "Bearer ".plus(prefManager.accessTokenFlow.value)
        )
    }
}