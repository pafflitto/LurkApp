package com.example.lurk.extensions

import androidx.compose.animation.core.*
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.shrinkClick(
    onClick: () -> Unit
): Modifier = composed {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.9f else 1f)
    val alpha by animateFloatAsState(if (pressed) 0.7f else 1f)

    this
        .scale(scale)
        .alpha(alpha)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = PressedIndication(
                pressedChanged = { pressed = it }
            ),
            onClick = onClick
        )
}

fun Modifier.bounceClick(
    onClick: () -> Unit
): Modifier = composed {
    var pressed by remember { mutableStateOf(false) }
    val transition = updateTransition(pressed, label = "Scaling Text")
    val animatedTextScale by transition.animateFloat(
        transitionSpec = { spring(stiffness = 900f, dampingRatio = Spring.DampingRatioMediumBouncy) },
        label = ""
    ) { if (it) 1.15f else 1f }

    val alpha by transition.animateFloat(label = "animated transparency") {
        if (it) 0.75f else 1f
    }

    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = PressedIndication { pressed = it },
        onClick = onClick
    )
    .graphicsLayer(scaleX = animatedTextScale, scaleY = animatedTextScale)
    .alpha(alpha)
}

fun Modifier.noIndicationClick(
    onClick: () -> Unit
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

private class PressedIndication(
    val pressedChanged: (Boolean) -> Unit
) : Indication {

    private inner class PressedIndication(
        private val isPressed: State<Boolean>,
    ) : IndicationInstance {

        override fun ContentDrawScope.drawIndication() {
            drawContent()
            pressedChanged(isPressed.value)
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val isPressed = interactionSource.collectIsPressedAsState()
        return remember(interactionSource) {
            PressedIndication(isPressed)
        }
    }
}
