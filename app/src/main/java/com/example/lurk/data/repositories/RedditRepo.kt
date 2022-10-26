package com.example.lurk.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lurk.RedditImageLoader
import com.example.lurk.data.api.RedditApi
import com.example.lurk.datastores.UserPreferencesDataStore
import com.example.lurk.extensions.toTitleCase
import com.example.lurk.screens.feed.Post
import com.example.lurk.viewmodels.SortingType
import com.example.lurk.viewmodels.UserSubreddit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RedditRepo : Repo {
    suspend fun getSubreddit(
        subreddit: String,
        scope: CoroutineScope
    ): Result<Feed>
    suspend fun subredditSearch(query: String): Result<List<String>>
    suspend fun userSubreddits(): Result<Map<String, List<UserSubreddit>>>
    suspend fun toggleFavoriteSubreddit(subreddit: String, shouldSave: Boolean)
}
/**
 * Repository to handle the content calls to Reddit's api
 */
class RedditRepoImpl(
    private val redditApi: RedditApi,
    private val authRepo: RedditAuthRepo,
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val imageLoader: RedditImageLoader
) : RedditRepo {

    inner class PagingLocation(
        val before: String?,
        val after: String?
    )

    override suspend fun getSubreddit(
        subreddit: String,
        scope: CoroutineScope
    ): Result<Feed> {
        var failure: Throwable? = null
        val source = RedditPagingSource(
            subreddit = subreddit,
            imageLoader = imageLoader,
            scope = scope ,
            apiCall = { before, after ->
                apiCall(authRepo) {
                    redditApi.subredditRequest(
                        headerMap = getAuthHeader(),
                        subreddit = subreddit,
                        before = before,
                        after = after
                    )
                }
            },
            apiFailure = {
                failure = it
            }
        )

        val postsFlow = Pager(
            config = PagingConfig(pageSize = 50),
            pagingSourceFactory = { source }
        ).flow.cachedIn(scope)

        return failure?.let { Result.failure(it) } ?: Result.success(Feed(subreddit.toTitleCase(), postsFlow))
    }

    override suspend fun subredditSearch(query: String): Result<List<String>> = apiCall(authRepo) {
            redditApi.subredditSearch(getAuthHeader(), query).subreddits.map { it.name.toTitleCase() }
    }

    override suspend fun userSubreddits(): Result<Map<String, List<UserSubreddit>>> {
        apiCall(authRepo) {
            if (authRepo.getUserAccountType() is AccountType.SignedInUser) {
                redditApi.userSubreddits(getAuthHeader())
            } else {
                redditApi.userlessSubreddits(getAuthHeader())
            }
        }.onSuccess { response ->
            val favoriteSubreddits = userPreferencesDataStore.favoriteSubredditFlow.value

            // overall map of all subreddits shown in the list
            val subredditMap = linkedMapOf<String, List<UserSubreddit>>()

            // Add General subreddits
            subredditMap[""] =
                listOf(UserSubreddit("Popular", null), UserSubreddit("Home", null))

            // List of favorite UserSubreddit objects
            val favSubredditList = mutableListOf<UserSubreddit>()

            // convert the http response to UserSubreddits and update the favSubreddit list
            val userSubreddits = response.data.children.map {
                val url = it.data.url
                val displayName =
                    url.substring(3, url.length - 1).replaceFirstChar(Char::titlecase)
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

            return Result.success(subredditMap)
        }.onFailure {
            return Result.failure(it)
        }

        return Result.failure(Throwable("Unexpected Error"))
    }

    override suspend fun toggleFavoriteSubreddit(subreddit: String, shouldSave: Boolean) = userPreferencesDataStore
        .saveRemoveFavoriteSubreddit(subreddit = subreddit, shouldSave = true)


    private suspend fun getAuthHeader(): HashMap<String, String> = hashMapOf(
        "User-Agent" to "Lurk for Reddit",
        "Authorization" to "Bearer ".plus(authRepo.getAccessToken())
    )
}

data class Feed(
    val subreddit: String,
    val postFlow: Flow<PagingData<Post>>,
    val sortingType: SortingType = SortingType.BEST
)