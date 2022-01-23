package com.example.lurk.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lurk.screens.feed.Post

class FeedSource(
    val subreddit: String
) : PagingSource<String, Post>() {
    private val repo = FeedRepo()

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
        val response = repo.requestSubreddit(
            subreddit = subreddit,
            after = if (params is LoadParams.Append) params.key else null,
            before = if (params is LoadParams.Prepend) params.key else null,
        )

        val posts = response?.data?.children?.map {
            Post(
                title = it.data.title,
                author = it.data.author,
                subreddit = it.data.subreddit,
                type = Post.typeForPostHint(it.data.postHint),
                comments = it.data.numComments,
                ups = it.data.ups,
                downs = it.data.downs,
                url = it.data.url
            )
        } ?: emptyList()

        return LoadResult.Page(
            data = posts,
            prevKey = response?.data?.before,
            nextKey = response?.data?.after
        )
    }
}