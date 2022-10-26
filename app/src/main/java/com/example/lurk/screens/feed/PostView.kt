package com.example.lurk.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lurk.screens.feed.expanded_media_screen.ExpandedMedia
import com.example.lurk.screens.feed.postviews.*
import com.example.lurk.ui.theme.Extended
import com.google.android.exoplayer2.ExoPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostView(
    key: String,
    post: Post,
    subredditSelected: (String) -> Unit = {},
    updateVoteStatus: (Post.Companion.Voted) -> Unit = {},
    expandMedia: (ExpandedMedia) -> Unit = { _ ->},
    expandedMedia: ExpandedMedia? = null,
    gifExoPlayers: Map<String, ExoPlayer>
) {
    var voted by remember { mutableStateOf(post.voted) }
    val clicked by remember { mutableStateOf(post.clicked) }

    LaunchedEffect(voted) {
        updateVoteStatus(voted)
    }

    LaunchedEffect(clicked) {
        // TODO Add in vm function here to update the post
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Extended.getPostBackgroundColor()
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (post.clicked) 0.dp else 4.dp,
            pressedElevation = 0.dp
        ),
        onClick = {}
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
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
                        showBlank = expandedMedia?.post?.id == post.id,
                        exoPlayer = gifExoPlayers[key]
                    )
                }
                else -> {}// Title and footer only
            }

            Footer(
                post = post,
                voted = voted,
                upVoteClick = {
                    voted = if (voted == Post.Companion.Voted.UpVoted) Post.Companion.Voted.NoVote else Post.Companion.Voted.UpVoted
                },
                downVoteClick = {
                    voted = if (voted == Post.Companion.Voted.DownVoted) Post.Companion.Voted.NoVote else Post.Companion.Voted.DownVoted
                }
            )
        }
    }
}