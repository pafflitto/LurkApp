package com.example.lurk.screens.feed

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.lurk.screens.feed.Post.Companion.Voted
import com.example.lurk.screens.feed.Post.Companion.Voted.*
import com.example.lurk.screens.feed.post_views.GifPostView
import com.example.lurk.screens.feed.post_views.ImagePostView
import com.example.lurk.screens.feed.post_views.TextPostView
import com.example.lurk.ui.theme.Extended
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.MainPageScreen
import com.example.lurk.viewmodels.FeedViewModel
import com.example.lurk.viewmodels.LoadingState
import com.google.android.exoplayer2.ExoPlayer
import kotlin.math.roundToInt

@Composable
fun FeedScreen(
    feed: FeedViewModel.Feed?,
    listState: LazyListState,
    loadingState: LoadingState,
    updateVoteStatus: (Voted) -> Unit,
    subredditSelected: (String) -> Unit,
    expandMedia: (Post) -> Unit,
    gifExoPlayers: SnapshotStateMap<String, ExoPlayer>,
    updateTopVisibleItems: (Map<Pair<String, Boolean>, String>) -> Unit
)
{
    val subredditTextSize by animateFloatAsState(
        if (listState.firstVisibleItemIndex != 0 || listState.firstVisibleItemScrollOffset > 0) {
            24f
        }
        else {
            MaterialTheme.typography.displaySmall.fontSize.value
        },
        animationSpec = tween(300)
    )


    if (feed != null) {
        val posts = feed.postFlow.collectAsLazyPagingItems()
        MainPageScreen(
            title = feed.subreddit,
            titleFontSize = subredditTextSize.sp
        ) {
            Box(Modifier.fillMaxSize()) {
                if (loadingState == LoadingState.LOADED) {
                    Posts(
                        posts = posts,
                        updateVoteStatus = updateVoteStatus,
                        listState = listState,
                        expandMedia = expandMedia,
                        subredditSelected = subredditSelected,
                        gifExoPlayers = gifExoPlayers,
                        updateTopVisibleItems = updateTopVisibleItems
                    )
                } else {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun Posts(
    posts: LazyPagingItems<Post>,
    updateVoteStatus: (Voted) -> Unit,
    listState: LazyListState,
    expandMedia: (Post) -> Unit,
    subredditSelected: (String) -> Unit,
    gifExoPlayers: Map<String, ExoPlayer>,
    updateTopVisibleItems: (Map<Pair<String, Boolean>, String>) -> Unit,
    ) {

    val centerDisplayRange = with(LocalDensity.current) {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp.toPx()
        val centerPx = (screenHeight / 2)
        val centerPadding = screenHeight * 0.15
        (centerPx - centerPadding).roundToInt()..(centerPx + centerPadding).roundToInt()
    }

    AnimatedVisibility(
        visible = posts.itemCount > 0,
        enter = slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
        ),
        exit = fadeOut()
    ) {

        LaunchedEffect(Unit) {
            snapshotFlow {
                // Flow to tell the exoplayer map which gifs to play
                listState.layoutInfo.visibleItemsInfo
            }.collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    // Get items with a padding of two to load into the map.
                    // This will load gifs that are about to come onto the screen
                    val firstItemIndex = if (visibleItems.first().index - 5 < 0) 0 else visibleItems.first().index - 5
                    val itemsToLoad = firstItemIndex until visibleItems.last().index + 2

                    // Create the map to send to the exoplayer map that will load these items
                    val gifMap = visibleItems.filter { itemsToLoad.contains(it.index) }.mapNotNull { item ->
                        posts[item.index]?.let { post ->
                            if (post is GifPost) {
                                // Key is a pair, first is the index and second is if we should play the gif
                                // We will only play a gif if the top or bottom of it is within 30% padding of the center of the display
                                val itemRange = item.offset..(item.offset + item.size)
                                val shouldPlay = itemRange.first <= centerDisplayRange.last && itemRange.last >= centerDisplayRange.first
                                Pair(post.idForIndex(item.index), shouldPlay) to post.url
                            }
                            else null
                        }
                    }.toMap()
                    updateTopVisibleItems(gifMap)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
            state = listState
        ) {
            itemsIndexed(
                items = posts,
                key = {index, item -> "$index-${item.id}"}
            ) { index, post ->
                if (post != null) {
                    PostView(
                        key = post.idForIndex(index),
                        post = post,
                        updateVoteStatus = updateVoteStatus,
                        expandMedia = expandMedia,
                        subredditSelected = {
                            subredditSelected(it)
                        },
                        gifExoPlayers = gifExoPlayers
                    )
                }
            }
        }
    }
}

@Composable
private fun PostView(
    key: String,
    post: Post,
    subredditSelected: (String) -> Unit = {},
    updateVoteStatus: (Voted) -> Unit = {},
    expandMedia: (Post) -> Unit = {},
    gifExoPlayers: Map<String, ExoPlayer>
) {
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
                subredditSelected = subredditSelected,
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
                    expandMedia = expandMedia
                )
                is GifPost -> {
                    GifPostView(
                        post = post,
                        expandMedia = expandMedia,
                        exoPlayer = gifExoPlayers[key]
                    )
                }
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
                    PostView(post = Post.exampleTextPost, gifExoPlayers = emptyMap(), key = "")
                }
            }
        }
    }
}

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
                    PostView(post = Post.exampleTextPost, gifExoPlayers = emptyMap(), key = "")
                }
            }
        }
    }
}