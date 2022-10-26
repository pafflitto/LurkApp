package com.example.lurk.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import com.example.lurk.screens.feed.Post.Companion.Voted.*
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.screens.feed.Post.Companion as Post

@Composable
fun AnimatedVoteBackground(
    modifier: Modifier = Modifier,
    voteState: Post.Voted = NoVote,
) {
    Box(
        modifier = modifier.clipToBounds()
    ) {
        AnimatedVisibility(
            visible = voteState == UpVoted,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, Spring.StiffnessLow)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, Spring.StiffnessLow)
            ),
        ) {
            Spacer(modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
        }
        AnimatedVisibility(
            visible = voteState == DownVoted,
            enter = expandVertically (
                expandFrom = Alignment.Top,
                clip = true,
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, Spring.StiffnessLow)
            ),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Top,
                clip = true,
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, Spring.StiffnessLow)
            ),
        ) {
            Spacer(modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.errorContainer)
            )
        }
    }
}

@Preview
@Composable
fun AnimatedVoteBackground() {
    LurkTheme {
        AnimatedVoteBackground(voteState = UpVoted)
    }
}