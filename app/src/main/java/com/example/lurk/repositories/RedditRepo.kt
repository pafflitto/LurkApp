package com.example.lurk.repositories

import ListingResponse
import com.example.lurk.*
import com.example.lurk.retrofit_clients.RedditClient
import com.example.lurk.retrofit_clients.handleRequest
import com.example.lurk.viewmodels.UserSubreddit

/**
 * Repository to handle the content calls to Reddit's api
 */
class RedditRepo {

    private val redditClient = RedditClient.webservice
    private val authManager = LurkApplication.instance().authManager
    private val prefManager = LurkApplication.instance().authPrefDataStore

    // TODO RETURN ERRORS AND HANDLE THOSE BAD BOYS
    suspend fun requestSubreddit(
        subreddit: String?,
        after: String?,
        before: String?
    ): ListingResponse? {
        if (!authManager.userHasAccess.value) return null

        var result: ListingResponse? = null
        handleRequest {
            redditClient.subredditRequest(authHeader, subreddit ?: "popular", after, before)
        }.onSuccess { response ->
            result = response
        }

        return result
    }

    suspend fun getUserSubreddits(): Map<String, List<UserSubreddit>>? {
        if (!authManager.userHasAccess.value) return null

        var result: Map<String, List<UserSubreddit>> = emptyMap()
        handleRequest {
            if (authDataStore.oUserSignedIn.value) {
                redditClient.userSubreddits(authHeader)
            } else {
                redditClient.userlessSubreddits(authHeader)
            }
        }.onSuccess { response ->
            val favoriteSubreddits = userPrefDataStore.favoriteSubredditFlow.value

            // overall map of all subreddits shown in the list
            val subredditMap = linkedMapOf<String, List<UserSubreddit>>()

            // Add General subreddits
            subredditMap[""] = listOf(UserSubreddit("Popular", null), UserSubreddit("Home", null))

            // List of favorite UserSubreddit objects
            val favSubredditList = mutableListOf<UserSubreddit>()

            // convert the http response to UserSubreddits and update the favSubreddit list
            val userSubreddits = response.data.children.map {
                val url = it.data.url
                val displayName = url.substring(3, url.length - 1).replaceFirstChar(Char::titlecase)
                val isFavorite = favoriteSubreddits.contains(displayName.lowercase())

                val userSubreddit = UserSubreddit(name = displayName, favorited = isFavorite)

                if (isFavorite) {
                    favSubredditList.add(userSubreddit)
                }

                userSubreddit
            }.sortedBy { it.name }.groupBy { it.name.first().toString() }

            if (favSubredditList.isNotEmpty()) {
                subredditMap["Favorites"] = favSubredditList.sortedBy { it.name }
            }

            subredditMap.putAll(userSubreddits)

            result = subredditMap
        }

        return result
    }

    suspend fun subredditSearch(query: String): List<String> {
        var result = emptyList<String>()
        handleRequest {
            redditClient.subredditSearch(authHeader, query).subreddits.map { it.name.toTitleCase() }
        }.onSuccess { response ->
            result = response
        }
        return result
    }

    private val authHeader: HashMap<String, String> get() {
        return hashMapOf(
            "User-Agent" to "Lurk for Reddit",
            "Authorization" to "Bearer ".plus(prefManager.oAccessToken.value)
        )
    }
}