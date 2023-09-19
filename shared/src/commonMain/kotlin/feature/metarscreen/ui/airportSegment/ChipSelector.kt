package feature.metarscreen.ui.airportSegment

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

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
            color = Color(0xff00c853),
            topLeft = Offset(selectorRect.value.left, selectorRect.value.top),
            size = selectorRect.value.size,
            style = Stroke(width = 4.dp.toPx()),
            cornerRadius = CornerRadius(30.dp.toPx())
        )
    }
}