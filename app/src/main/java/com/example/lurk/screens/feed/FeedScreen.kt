package com.example.lurk.screens.feed

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.example.lurk.screens.feed.Post.Companion.Voted
import com.example.lurk.screens.feed.Post.Companion.Voted.*
import com.example.lurk.screens.feed.post_views.ImagePostView
import com.example.lurk.screens.feed.post_views.TextPostView
import com.example.lurk.ui.theme.LurkTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun FeedScreen(
    subreddit: String = "Home",
    posts: Flow<PagingData<Post>>,
    updateVoteStatus: (Voted) -> Unit
)
{
    val lazyPosts = posts.collectAsLazyPagingItems()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = subreddit,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        Posts(
            posts = lazyPosts,
            updateVoteStatus = updateVoteStatus
        )
    }
}

@Composable
fun Posts(
    posts: LazyPagingItems<Post>,
    updateVoteStatus: (Voted) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(posts) { post ->
            if (post != null) {
                PostView(
                    post = post,
                    updateVoteStatus = updateVoteStatus
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostView(
    post: Post,
    updateVoteStatus: (Voted) -> Unit
)
{
    var voted by remember { mutableStateOf(post.voted) }
    val clicked by remember { mutableStateOf(post.clicked)}

    LaunchedEffect(voted) {
        updateVoteStatus(voted)
    }

    LaunchedEffect(clicked) {
        // TODO Add in vm function here to update the post
    }
    val borderColor by animateColorAsState(
        when {
            voted == UpVoted -> MaterialTheme.colorScheme.secondary
            voted == DownVoted -> MaterialTheme.colorScheme.tertiary
            clicked -> MaterialTheme.colorScheme.outline
            else -> MaterialTheme.colorScheme.outline
        }
    )

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.outlinedCardElevation(pressedElevation = 16.dp),
        onClick = {
            // do nothing yet
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RedditTitle(
                post = post,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
            // Content
            when (post) {
                is TextPost -> TextPostView(
                    post = post,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                is ImagePost -> ImagePostView(
                    post = post,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                else -> {}// Title and footer only
            }

            Footer(
                post = post,
                voted = voted,
                upVoteClick = {
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
                    PostView(post = Post.exampleTextPost) {}
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
                    PostView(post = Post.exampleTextPost) {}
                }
            }
        }
    }
}