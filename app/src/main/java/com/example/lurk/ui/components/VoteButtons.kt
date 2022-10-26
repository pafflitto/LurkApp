package com.example.lurk.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.R
import com.example.lurk.ui.theme.LurkTheme

@Composable
fun UpvoteButton(
    selected: Boolean,
    onClick: () -> Unit,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(
            id = if (!selected) R.drawable.ic_upvote else R.drawable.ic_upvote_selected
        ),
        contentDescription = "Upvote",
        tint = iconColor,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .size(24.dp)
    )
}

@Composable
fun DownVoteButton(
    selected: Boolean,
    onClick: () -> Unit,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(
            id = if (!selected) R.drawable.ic_downvote else R.drawable.ic_downvote_selected
        ),
        contentDescription = "Downvote",
        tint = iconColor,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .size(24.dp)
    )
}

@Preview
@Composable
fun UpvoteButtonPreview() {
    LurkTheme {
        Surface {
            Column {
                Row {
                    UpvoteButton(
                        selected = true,
                        onClick = {},
                        iconColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                    )
                    UpvoteButton(
                        selected = false,
                        onClick = {},
                        iconColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                    )
                    DownVoteButton(
                        selected = true,
                        onClick = {},
                        iconColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                    )
                    DownVoteButton(
                        selected = false,
                        onClick = {},
                        iconColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}