package com.example.lurk.screens.feed

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.example.lurk.screens.feed.Post.Companion.Voted
import com.example.lurk.screens.feed.Post.Companion.Voted.*
import com.example.lurk.screens.feed.post_views.ImagePostView
import com.example.lurk.screens.feed.post_views.TextPostView
import com.example.lurk.ui.theme.Extended
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.ContentScreen

@Composable
fun FeedScreen(
    subreddit: String = "Home",
    posts: LazyPagingItems<Post>,
    updateVoteStatus: (Voted) -> Unit,
)
{
    val listState = rememberLazyListState()
    val subredditTextSize by animateFloatAsState(
        if (listState.firstVisibleItemIndex != 0) {
            24f
        }
        else {
            MaterialTheme.typography.displaySmall.fontSize.value
        },
        animationSpec = tween(300)
    )

    ContentScreen(
        title = subreddit,
        titleFontSize = subredditTextSize.sp
    ) {
        Posts(
            posts = posts,
            updateVoteStatus = updateVoteStatus,
            listState = listState
        )
    }
}

@Composable
fun Posts(
    posts: LazyPagingItems<Post>,
    updateVoteStatus: (Voted) -> Unit,
    listState: LazyListState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(4.dp),
        state = listState
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Extended.PostBackgroundColor
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (post.clicked) 0.dp else 4.dp,
            pressedElevation = 0.dp
        ),
        onClick = {}
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
    LurkTheme(useDarkPreviewTheme = true) {
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
    LurkTheme(useDarkPreviewTheme = false) {
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