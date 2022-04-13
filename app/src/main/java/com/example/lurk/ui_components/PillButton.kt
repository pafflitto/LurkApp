package com.example.lurk.ui_components

import android.content.res.Configuration
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.ui.theme.LurkTheme

@ExperimentalComposeUiApi
@Composable
fun PillButton(
    onClick: () -> Unit = {},
    text: String
) {

    var currentState by remember { mutableStateOf(ButtonState.RELEASED) }
    val transition = updateTransition(currentState, label = "Scaling Text")

    val scale by transition.animateFloat(label = "animated scale") {
        if (it == ButtonState.PRESSED) 0.9f else 1f
    }
    val alpha by transition.animateFloat(label = "animated alpha") {
        if (it == ButtonState.PRESSED) 0.7f else 1f
    }

    Box(
        modifier = Modifier
            .scale(scale)
            .alpha(alpha)
    )
    {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .pointerInteropFilter {
                    currentState = when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            ButtonState.PRESSED
                        }
                        MotionEvent.ACTION_MOVE -> currentState
                        MotionEvent.ACTION_UP -> {
                            if (currentState == ButtonState.PRESSED) {
                                onClick
                            }
                            ButtonState.RELEASED
                        }
                        else -> ButtonState.RELEASED
                    }
                    true
                }
                .background(color = MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
        )
    }
}

@ExperimentalComposeUiApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PillButtonPreviewLight() {
    LurkTheme(useDarkTheme = false) {
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
    LurkTheme(useDarkTheme = true) {
        Surface {
            PillButton(
                text = "Text Button"
            )
        }
    }
}