package com.example.lurk.ui_components

import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward30
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay30
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.findActivity
import com.example.lurk.ui.theme.LurkTheme

@Composable
fun PlayerView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val window = context.findActivity()?.window
        window?.addFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    LurkTheme(true) {
        Column(
            modifier
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp, top = 48.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Rounded.Replay30,
                    contentDescription = "Replay 30",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.width(48.dp))
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = "Play Video",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.width(48.dp))
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Rounded.Forward30,
                    contentDescription = "Forward 30",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
    }
}

@Preview
@Composable
private fun PlayerViewLight()
{
    LurkTheme(useDarkPreviewTheme = false) {
        PlayerView()
    }
}

@Preview
@Composable
private fun PlayerViewDark()
{
    LurkTheme(useDarkPreviewTheme = true) {
        PlayerView()
    }
}