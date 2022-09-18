package com.example.lurk.screens.feed

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.R
import com.example.lurk.screens.feed.Post.Companion.Voted
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.AnimatedVoteBackground
import com.example.lurk.ui_components.DownVoteButton
import com.example.lurk.ui_components.UpvoteButton

@Composable
fun Footer(
    post: Post,
    modifier: Modifier = Modifier,
    voted: Voted,
    upVoteClick: () -> Unit,
    downVoteClick: () -> Unit
) {
    val itemColor by animateColorAsState(
        when(voted) {
            Voted.UpVoted -> MaterialTheme.colorScheme.onPrimaryContainer
            Voted.DownVoted -> MaterialTheme.colorScheme.onErrorContainer
            else -> MaterialTheme.colorScheme.onSurface
        }
    )

    Column {
        Box(
            modifier = modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            AnimatedVoteBackground(
                modifier = Modifier.fillMaxSize(),
                voteState = voted
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = if (post.totalComments >= 0) R.drawable.ic_upvote_selected else R.drawable.ic_downvote_selected),
                            contentDescription = "Votes icon",
                            tint = itemColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = post.votes,
                            style = MaterialTheme.typography.labelMedium,
                            color = itemColor,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_comment),
                            contentDescription = "Comment Icon",
                            tint = itemColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = post.commentsString,
                            style = MaterialTheme.typography.labelMedium,
                            color = itemColor,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    UpvoteButton(
                        selected = voted == Voted.UpVoted,
                        onClick = upVoteClick,
                        iconColor = itemColor,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    DownVoteButton(
                        selected = voted == Voted.DownVoted,
                        onClick = downVoteClick,
                        iconColor = itemColor,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FooterLightPreview() {
    LurkTheme(useDarkPreviewTheme = false) {
        Surface {
            Footer(
                post = Post.exampleTextPost,
                modifier = Modifier.padding(horizontal = 16.dp),
                voted = Voted.NoVote,
                upVoteClick = {},
                downVoteClick = {}
            )
        }
    }
}

@Preview
@Composable
fun FooterDarkPreview() {
    LurkTheme(useDarkPreviewTheme = true) {
        Surface {
            Footer(
                post = Post.exampleTextPost,
                modifier = Modifier.padding(horizontal = 16.dp),
                voted = Voted.NoVote,
                upVoteClick = {},
                downVoteClick = {}
            )
        }
    }
}