package com.example.lurk.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun ContentScreen(
    title: String,
    titleFontSize: TextUnit = MaterialTheme.typography.displaySmall.fontSize,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            fontSize = titleFontSize,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp, start = 16.dp),
            textAlign = TextAlign.Start
        )

        content()
    }
}