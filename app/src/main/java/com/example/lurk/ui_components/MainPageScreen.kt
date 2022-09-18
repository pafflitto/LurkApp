package com.example.lurk.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
fun MainPageScreen(
    title: String,
    titleFontSize: TextUnit = MaterialTheme.typography.displaySmall.fontSize,
    content: @Composable () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium,
            fontSize = titleFontSize,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(top = 0.dp, bottom = 8.dp, start = 16.dp),
            textAlign = TextAlign.Start
        )

        content()
    }
}