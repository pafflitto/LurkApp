package com.example.lurk.screens.feed

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.lurk.screens.feed.postviews.Post.Companion.Voted
import com.example.lurk.screens.feed.expanded_media_screen.ExpandedMedia
import com.example.lurk.screens.feed.postviews.GifPost
import com.example.lurk.screens.feed.postviews.Post
import com.example.lurk.ui.components.MainPageScreen
import com.example.lurk.ui.components.NavBarItem
import com.example.lurk.ui.components.pageEnterTransition
import com.example.lurk.ui.components.pageExitTransition
import com.example.lurk.ui.theme.LurkTheme
import com.google.accompanist.navigation.animation.composable
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.feedScreen(
    expandedMedia: ExpandedMedia?,
    expandedMediaChange: (ExpandedMedia) -> Unit,
    viewModel: FeedViewModel,
    listState: LazyListState
) = composable(
    route = NavBarItem.Home.route,
    enterTransition = pageEnterTransition(),
    exitTransition = pageExitTransition(),
) {

    FeedScreen(
        expandedMedia = expandedMedia,
        listState = listState,
        expandedMediaChange = expandedMediaChange,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FeedScreen(
    expandedMedia: ExpandedMedia?,
    expandedMediaChange: (ExpandedMedia) -> Unit,
    listState: LazyListState,
    viewModel: FeedViewModel,
) {
    val smallTitle by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex != 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }
    val subredditTextSize by animateFloatAsState(
        if (smallTitle) {
            24f
        } else {
            MaterialTheme.typography.displaySmall.fontSize.value
        },
        animationSpec = tween(300)
    )

    val scope = rememberCoroutineScope()
    val feedState by viewModel.feedStateFlow.collectAsState()
    val subreddit by viewModel.currentSubredditFlow.collectAsState()

    val postsState = when(val state = feedState) {
        is FeedState.Loading -> PostState.Loading
        is FeedState.Loaded -> {
            val posts = state.feed.postFlow.collectAsLazyPagingItems()
            if (posts.loadState.refresh is LoadState.Loading) {
                PostState.Loading
            } else PostState.Loaded(posts)
        }
        is FeedState.Error -> PostState.Error
    }

    MainPageScreen(
        title = subreddit,
        titleFontSize = subredditTextSize.sp
    ) {
        AnimatedContent(
            targetState = postsState,
            transitionSpec = {
                if (targetState is PostState.Loaded && initialState !is PostState.Loaded) {
                    slideInVertically { it / 10 } with fadeOut()
                } else {
                    fadeIn() with
                            scaleOut(targetScale = 0.95f) +
                            slideOutVertically { it / 10 } +
                            fadeOut(animationSpec = tween(200))
                }
            }
        ) { state ->
            when (state) {
                is PostState.Loading -> LoadingScreen()
                is PostState.Loaded ->
                        FeedScreenContent(
                            posts = state.posts,
                            listState = listState,
                            updateVoteStatus = viewModel::voteStatusUpdated,
                            subredditSelected = {
                                scope.launch {
                                    viewModel.updateSubreddit(it)
                                    delay(200)
                                    listState.scrollToItem(0)
                                }
                            },
                            expandMedia = expandedMediaChange,
                            expandedMedia = expandedMedia,
                            updateTopVisibleItems = viewModel::updateVisibleItems,
                            gifExoPlayers = viewModel.gifExoPlayers
                        )
                else -> {
                    // Nothing for now
                    Text("ERROR")
                }
            }
        }
    }
}


@Composable
fun FeedScreenContent(
    posts: LazyPagingItems<Post>,
    listState: LazyListState,
    updateVoteStatus: (Voted) -> Unit,
    subredditSelected: (String) -> Unit,
    expandMedia: (ExpandedMedia) -> Unit,
    expandedMedia: ExpandedMedia? = null,
    gifExoPlayers: SnapshotStateMap<String, ExoPlayer>,
    updateTopVisibleItems: (Map<Pair<String, Boolean>, String>) -> Unit
) {

    Posts(
        posts = posts,
        updateVoteStatus = updateVoteStatus,
        listState = listState,
        expandMedia = expandMedia,
        expandedMedia = expandedMedia,
        subredditSelected = subredditSelected,
        gifExoPlayers = gifExoPlayers,
        updateTopVisibleItems = updateTopVisibleItems
    )
}

@Composable
private fun LoadingScreen() = Box(Modifier.fillMaxSize()) {
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.align(Alignment.Center)
    )
}

@Composable
private fun Posts(
    posts: LazyPagingItems<Post>,
    updateVoteStatus: (Voted) -> Unit,
    listState: LazyListState,
    expandMedia: (ExpandedMedia) -> Unit,
    expandedMedia: ExpandedMedia?,
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


    LaunchedEffect(Unit) {
        snapshotFlow {
            // Flow to tell the exoplayer map which gifs to play
            listState.layoutInfo.visibleItemsInfo
        }.collect { visibleItems ->
            if (visibleItems.isNotEmpty()) {
                // Get items with a padding of two to load into the map.
                // This will load gifs that are about to come onto the screen
                val firstItemIndex =
                    if (visibleItems.first().index - 5 < 0) 0 else visibleItems.first().index - 5
                val itemsToLoad = firstItemIndex until visibleItems.last().index + 2

                // Create the map to send to the exoplayer map that will load these items
                val gifMap =
                    visibleItems.filter { itemsToLoad.contains(it.index) }.mapNotNull { item ->
                        if (item.index >= posts.itemCount) return@mapNotNull null
                        posts[item.index]?.let { post ->
                            if (post is GifPost) {
                                // Key is a pair, first is the index and second is if we should play the gif
                                // We will only play a gif if the top or bottom of it is within 30% padding of the center of the display
                                val itemRange = item.offset..(item.offset + item.size)
                                val shouldPlay =
                                    itemRange.first <= centerDisplayRange.last && itemRange.last >= centerDisplayRange.first
                                Pair(post.idForIndex(item.index), shouldPlay) to post.url
                            } else null
                        }
                    }.toMap()
                updateTopVisibleItems(gifMap)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(8.dp),
        state = listState
    ) {
        itemsIndexed(
            items = posts,
            key = { index, item -> "$index-${item.id}" }
        ) { index, post ->
            if (post != null) {
                PostView(
                    key = post.idForIndex(index),
                    post = post,
                    updateVoteStatus = updateVoteStatus,
                    expandMedia = expandMedia,
                    expandedMedia = if (post.id == expandedMedia?.post?.id) expandedMedia else null,
                    subredditSelected = {
                        subredditSelected(it)
                    },
                    gifExoPlayers = gifExoPlayers
                )
            }
        }

        if (posts.loadState.append is LoadState.Loading) {
            item {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FeedScreenPreviewDark() {
    LurkTheme(useDarkPreviewTheme = true) {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
fun FeedScreenPreviewLight() {
    LurkTheme(useDarkPreviewTheme = false) {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(5)
                {
                    PostView(post = Post.exampleTextPost, gifExoPlayers = emptyMap(), key = "")
                }
            }
        }
    }
}

private sealed class PostState {
    object Loading : PostState()
    data class Loaded(val posts: LazyPagingItems<Post>) : PostState()
    object Error : PostState()
}