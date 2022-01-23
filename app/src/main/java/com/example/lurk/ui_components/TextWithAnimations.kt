package com.example.lurk.ui_components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight

@Composable
fun BouncingText(
    text: String,
    modifier: Modifier,
    clickEvent: () -> Unit
)
{
    var currentState by remember { mutableStateOf(ButtonState.RELEASED) }
    val transition = updateTransition(currentState, label = "Scaling Text")

    val animatedTextScale by transition.animateFloat(
        transitionSpec = { spring(stiffness = 900f, dampingRatio = Spring.DampingRatioMediumBouncy) },
        label = ""
    ) {
        if (it == ButtonState.PRESSED) 1.15f else 1f
    }

    val alpha by transition.animateFloat(label = "animated transparency") {
        if (it == ButtonState.PRESSED) 0.75f else 1f
    }

    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    currentState = ButtonState.PRESSED
                    tryAwaitRelease()
                    currentState = ButtonState.RELEASED
                    clickEvent()
                })
            }
            .graphicsLayer(scaleX = animatedTextScale, scaleY = animatedTextScale)
            .alpha(alpha)
    )
}