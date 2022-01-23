package com.example.lurk.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.ui.theme.LurkTheme

@Composable
fun Footer(post: Post) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Divider(
            color = MaterialTheme.colors.secondary,
            thickness = 1.dp
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = post.votes)
        }
    }
}

@Preview
@Composable
fun FooterLightPreview() {
    LurkTheme(darkTheme = false) {
        Surface {
            Footer(
                post = Post.exampleTextPost
            )
        }
    }
}

@Preview
@Composable
fun FooterDarkPreview() {
    LurkTheme(darkTheme = false) {
        Surface {
            Footer(
                post = Post.exampleTextPost
            )
        }
    }
}