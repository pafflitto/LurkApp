package com.example.lurk.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.PillButton

@Composable
fun TextPostView(post: Post) {
    Column(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    ) {
        RedditTitle(
            text = post.title,
            modifier = Modifier
        )
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PillButton(
                clickEvent = {},
                text = "r/${post.subreddit}"
            )
            PillButton(
                clickEvent = {},
                text = "u/${post.author}"
            )
        }
    }
}

@Preview
@Composable
fun TextPostPreviewLight() {
    LurkTheme(darkTheme = false) {
        Surface {
            TextPostView(post = Post.exampleTextPost)
        }
    }
}

@Preview
@Composable
fun TextPostPreviewDark() {
    LurkTheme(darkTheme = true) {
        Surface {
            TextPostView(post = Post.exampleTextPost)
        }
    }
}
