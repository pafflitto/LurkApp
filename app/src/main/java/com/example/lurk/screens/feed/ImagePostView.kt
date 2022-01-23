package com.example.lurk.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.example.lurk.R
import com.example.lurk.ui.theme.LurkTheme

@ExperimentalCoilApi
@Composable
fun ImagePostView(post: Post) {
    Column {
        Image(
            painter = rememberImagePainter(
                data = post.url,
                builder = {
                    size(OriginalSize)
                    placeholder(R.drawable.ic_image_not_loaded)
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .heightIn(0.dp, 600.dp)
        )
        RedditTitle(
            text = post.title,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@ExperimentalCoilApi
@Preview
@Composable
fun PostContentPreviewDark() {
    LurkTheme(darkTheme = false) {
        Surface {
            ImagePostView(post = Post.exampleTextPost)
        }
    }
}