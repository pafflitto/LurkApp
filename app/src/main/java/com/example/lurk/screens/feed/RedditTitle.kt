package com.example.lurk.screens.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.PillButton

@ExperimentalComposeUiApi
@Composable
fun RedditTitle(
    post: Post,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = post.title,
            fontSize = 18.sp,
            modifier = modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
        )
        PillButton(
            clickEvent = {},
            text = "r/${post.subreddit}"
        )
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun RedditTitleLightPreview() {
    LurkTheme(darkTheme = false) {
        Surface {
            RedditTitle(
                post = Post.exampleTextPost,
                modifier = Modifier
            )
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun RedditTitleDarkPreview() {
    LurkTheme(darkTheme = true) {
        Surface {
            RedditTitle(
                post = Post.exampleTextPost,
                modifier = Modifier
            )
        }
    }
}