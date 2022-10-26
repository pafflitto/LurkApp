package com.example.lurk.data.repositories

import ListingResponse
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lurk.RedditImageLoader
import com.example.lurk.screens.feed.postviews.GifPost
import com.example.lurk.screens.feed.postviews.ImagePost
import com.example.lurk.screens.feed.postviews.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Class that holds the PagingSource for the feed page
 */
class RedditPagingSource(
    val subreddit: String,
    private val imageLoader: RedditImageLoader,
    private val scope: CoroutineScope,
    private val apiCall: suspend (before: String?, after: String?) -> Result<ListingResponse>,
    private val apiFailure: (Throwable) -> Unit,
) : PagingSource<String, Post>() {

    override val keyReuseSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        var posts = emptyList<Post>()
        var before: String? = null
        var after: String? = null
        apiCall(
            if (params is LoadParams.Prepend) params.key else null,
            if (params is LoadParams.Append) params.key else null,
        ).onSuccess {
            before = it.data.before
            after = it.data.after

            posts = it.data.children.map { children ->
                val post = Post.build(children.data)
                scope.launch(Dispatchers.IO) {
                    when (post) {
                        is GifPost -> {
                            post.thumbnail = post.thumbnailUrl?.let { url ->
                                imageLoader.loadImage(url)
                            }
                        }
                        is ImagePost -> {
                            post.image = imageLoader.loadImage(post.url)
                        }
                    }
                }
                post
            }
        }.onFailure {
            apiFailure(it)
        }

        return LoadResult.Page(
            data = posts,
            prevKey = before,
            nextKey = after
        )
    }
}