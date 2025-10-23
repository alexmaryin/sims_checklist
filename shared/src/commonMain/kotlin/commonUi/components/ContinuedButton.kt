package commonUi.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay

const val MAX_DELAY_MS = 400L
const val DELAY_FACTOR = 0.86f
const val MIN_DELAY_MS = 20L

@Composable
fun ContinuedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val currentClickListener by rememberUpdatedState(onClick)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse_scale"
    )

    Box(Modifier.graphicsLayer {
        if (pressed) {
            scaleX = scale
            scaleY = scale
            transformOrigin = TransformOrigin.Center
        }
    }) {
        TextButton(
            onClick = { /* Clicks are handled by pointerInput and LaunchedEffect */ },
            modifier = modifier.pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.type == PointerEventType.Press) pressed = true
                        if (event.type == PointerEventType.Release) pressed = false
                    }
                }
            },
            enabled = enabled,
            interactionSource = interactionSource,
            content = { content() }
        )
    }


    LaunchedEffect(pressed, enabled) {
        var currentDelayMillis = MAX_DELAY_MS

        while (pressed && enabled) {
            currentClickListener()
            delay(currentDelayMillis)
            currentDelayMillis = (currentDelayMillis * DELAY_FACTOR).toLong()
                .coerceAtLeast(MIN_DELAY_MS)
        }
    }
}