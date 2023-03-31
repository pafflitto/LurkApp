package com.example.lurk.screens.feed.postviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun VideoPostView(
    post: VideoPost
) {
    Box {
        Image(
            painter = rememberDrawablePainter(drawable = post.thumbnail),
            contentDescription = "Gif Thumbnail",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .heightIn(0.dp, 500.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )
        IconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Play Video"
            )
        }
    }
}