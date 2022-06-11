package com.example.lurk.screens.feed.post_views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.lurk.screens.feed.ImagePost
import com.example.lurk.screens.feed.Post
import com.example.lurk.ui.theme.LurkTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@ExperimentalCoilApi
@Composable
fun ImagePostView(
    post: ImagePost,
    expandMedia: (Post) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Image(
        painter = rememberDrawablePainter(drawable = post.image),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .clickable { expandMedia(post) }
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    )
}

@ExperimentalCoilApi
@Preview
@Composable
fun PostContentPreviewDark() {
    LurkTheme(useDarkPreviewTheme = false) {
        Surface {
            ImagePostView(post = Post.exampleImagePost)
        }
    }
}