package com.example.lurk.ui_components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.extensions.shrinkClick
import com.example.lurk.ui.theme.LurkTheme

@ExperimentalComposeUiApi
@Composable
fun PillButton(
    onClick: () -> Unit = {},
    text: String
) {
    Box(
        modifier = Modifier.shrinkClick(onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
        )
    }
}

@ExperimentalComposeUiApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PillButtonPreviewLight() {
    LurkTheme(useDarkPreviewTheme = false) {
        Surface {
            PillButton(
                text = "Text Button"
            )
        }
    }
}

@ExperimentalComposeUiApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PillButtonPreviewDark() {
    LurkTheme(useDarkPreviewTheme = true) {
        Surface {
            PillButton(
                text = "Text Button"
            )
        }
    }
}