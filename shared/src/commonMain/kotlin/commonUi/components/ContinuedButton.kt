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

// Progressive stepping thresholds
private const val PHASE_1_DURATION_MS = 3000L  // First 3s: step by 1
private const val PHASE_2_DURATION_MS = 3000L  // Next 3s (3-6s): step by 10
// After 6s: step by 100

@Composable
fun ContinuedButton(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val currentOnClick by rememberUpdatedState(onClick)

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
        if (!pressed || !enabled) return@LaunchedEffect

        val pressStartTime = System.currentTimeMillis()

        fun getStepSize(): Int {
            val elapsed = System.currentTimeMillis() - pressStartTime
            return when {
                elapsed < PHASE_1_DURATION_MS -> 1
                elapsed < PHASE_1_DURATION_MS + PHASE_2_DURATION_MS -> 10
                else -> 100
            }
        }

        val lastDelay = 100L

        while (pressed) {
            val stepSize = getStepSize()
            currentOnClick(stepSize)
            delay(lastDelay)
        }
    }
}
