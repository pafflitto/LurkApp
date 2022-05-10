package com.example.lurk.screens.feed

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.PillButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RedditTitle(
    post: Post,
    subredditSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = post.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            PillButton(
                text = post.subreddit,
                onClick = {
                    subredditSelected(post.subreddit.substring(2))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            PillButton(
                text = "u/${post.author}"
            )
        }
    }
}

@Preview
@Composable
fun RedditTitleLightPreview() {
    LurkTheme(useDarkPreviewTheme = false) {
        Surface {
            RedditTitle(
                post = Post.exampleTextPost,
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
fun RedditTitleDarkPreview() {
    LurkTheme(useDarkPreviewTheme = true) {
        Surface {
            RedditTitle(
                post = Post.exampleTextPost,
                modifier = Modifier
            )
        }
    }
}