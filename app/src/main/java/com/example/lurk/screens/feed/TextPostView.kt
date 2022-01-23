package com.example.lurk.screens.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.ui.theme.LurkTheme

@Composable
fun TextPostView(post: Post) {
    Column(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    ) {

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

