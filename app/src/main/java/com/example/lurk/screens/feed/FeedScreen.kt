package com.example.lurk.screens.feed

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.example.lurk.screens.feed.Post.Companion.Voted.*
import com.example.lurk.screens.feed.post_views.ImagePostView
import com.example.lurk.screens.feed.post_views.TextPostView
import com.example.lurk.ui.theme.LurkTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun FeedScreen(
    posts: Flow<PagingData<Post>>
)
{
    val lazyPosts = posts.collectAsLazyPagingItems()
    Posts(
        lazyPosts
    )
}

@Composable
fun Posts(posts: LazyPagingItems<Post>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(posts) { post ->
            if (post != null) {
                PostView(post = post)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostView(post: Post)
{
    var voted by remember { mutableStateOf(NoVote) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RedditTitle(
                post = post,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Content
            when (post) {
                is TextPost -> TextPostView(
                    post = post,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                is ImagePost -> ImagePostView(
                    post = post
                )
                else -> {}// Title and footer only
            }

            Footer(
                post = post,
                voted = voted,
                upVoteClick = {
                    // Use vm to update post with server here
                    voted = if (voted == UpVoted) NoVote else UpVoted
                },
                downVoteClick = {
                    voted = if (voted == DownVoted) NoVote else DownVoted
                }
            )
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FeedScreenPreviewDark()
{
    LurkTheme(useDarkTheme = true) {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(5)
                {
                    PostView(post = Post.exampleTextPost)
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun FeedScreenPreviewLight()
{
    LurkTheme(useDarkTheme = false) {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(5)
                {
                    PostView(post = Post.exampleTextPost)
                }
            }
        }
    }
}