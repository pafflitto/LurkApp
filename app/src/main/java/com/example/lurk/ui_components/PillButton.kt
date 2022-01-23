package com.example.lurk.ui_components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lurk.ui.theme.LurkTheme

@Composable
fun PillButton(
    clickEvent: () -> Unit,
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
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    currentState = ButtonState.PRESSED
                    tryAwaitRelease()
                    currentState = ButtonState.RELEASED
                    clickEvent()
                })
            }
    )
    {
        Text(
            text = text,
            style = MaterialTheme.typography.overline,
            fontSize = 10.sp,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(50))
                .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 2.dp)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PillButtonPreviewLight() {
    LurkTheme(darkTheme = false) {
        Surface {
            PillButton(
                clickEvent = {},
                text = "Text Button"
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PillButtonPreviewDark() {
    LurkTheme(darkTheme = true) {
        Surface {
            PillButton(
                clickEvent = {},
                text = "Text Button"
            )
        }
    }
}