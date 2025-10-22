package commonUi.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import commonUi.utils.SimColors

@Composable
fun ChipSelector(rect: Rect) {

    if (rect == Rect.Zero) return

    val selectorRect = animateRectAsState(
        targetValue = rect,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )

    Canvas(
        modifier = Modifier
    ) {
        drawRoundRect(
            color = SimColors.accentGreen,
            topLeft = Offset(selectorRect.value.left, selectorRect.value.top),
            size = selectorRect.value.size,
            style = Stroke(width = 4.dp.toPx()),
            cornerRadius = CornerRadius(30.dp.toPx())
        )
    }
}