package com.example.lurk.screens.feed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RedditTitle(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        fontSize = 18.sp,
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    )
}