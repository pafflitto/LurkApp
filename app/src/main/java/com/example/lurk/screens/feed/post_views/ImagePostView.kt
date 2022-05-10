package com.example.lurk.screens.feed.post_views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.example.lurk.R
import com.example.lurk.screens.feed.ImagePost
import com.example.lurk.screens.feed.Post
import com.example.lurk.ui.theme.LurkTheme

@ExperimentalCoilApi
@Composable
fun ImagePostView(
    post: ImagePost,
    expandMedia: (Post) -> Unit = {},
    modifier: Modifier = Modifier
) {
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
        modifier = modifier
            .clickable { expandMedia(post) }
            .heightIn(0.dp, 600.dp)
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