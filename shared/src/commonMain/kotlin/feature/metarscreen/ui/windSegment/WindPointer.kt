package feature.metarscreen.ui.windSegment

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import commonUi.loadXmlPicture

val ARROW_PADDING = CIRCLE_PADDING + LONG_STROKE + 60.dp

@Composable
fun WindPointer(boxScope: BoxWithConstraintsScope, angle: Int, color: Color) {
    boxScope.apply {

        var logicalTarget by remember { mutableFloatStateOf(angle.toFloat()) }

        LaunchedEffect(angle) {
            val currentAngle = logicalTarget
            var delta = angle - currentAngle
            if (delta > 180) delta -= 360
            if (delta < -180) delta += 360
            logicalTarget += delta
        }

        val animated by animateFloatAsState(
            targetValue = logicalTarget,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        // The pointer should point *from* the wind's direction, so we add 180 degrees.
        val drawAngle = animated + 180f

        Image(
            imageVector = loadXmlPicture("ic_wind_vane"),
            contentDescription = "Wind vane",
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(drawAngle)
                .size(min(maxWidth, maxHeight) - ARROW_PADDING),
            colorFilter = ColorFilter.tint(color)
        )
    }
}
