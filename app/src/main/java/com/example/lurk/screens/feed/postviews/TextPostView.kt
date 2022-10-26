package com.example.lurk.screens.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.ui.theme.LurkTheme

@Composable
fun TextPostView(
    post: TextPost,
    modifier: Modifier
) {
    if (post.text.isNotBlank()) {
        Column(
            modifier = modifier.heightIn(0.dp, 100.dp)
        ) {
            Text(
                text = post.text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun TextPostPreviewLight() {
    LurkTheme(useDarkPreviewTheme = false) {
        Surface {
            TextPostView(
                post = Post.exampleTextPost,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun TextPostPreviewDark() {
    LurkTheme(useDarkPreviewTheme = true) {
        Surface {
            TextPostView(
                post = Post.exampleTextPost,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

