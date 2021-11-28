package feature.metarscreen.ui.windSegment

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import ui.loadXmlPicture

val ARROW_PADDING = CIRCLE_PADDING + LONG_STROKE + 60.dp

@Composable
fun WindPointer(boxScope: BoxWithConstraintsScope, angle: Int, color: Color) {
    boxScope.apply {

        val animatedAngle = animateFloatAsState(
            targetValue = (if (angle >= 180) angle - 180 else angle + 180).toFloat(),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        Image(
            imageVector =  loadXmlPicture("ic_wind_vane"),
            contentDescription = "Wind vane",
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(animatedAngle.value)
                .size(min(maxWidth, maxHeight) - ARROW_PADDING),
            colorFilter = ColorFilter.tint(color)
        )
    }
}
