package feature.metarscreen.ui.windSegment

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.min
import ui.loadXmlPicture

@Composable
fun Runway(boxScope: BoxWithConstraintsScope, angle: Int) {
    boxScope.apply {

        val animatedAngle = animateFloatAsState(
            targetValue = (if (angle >= 180) angle - 180 else angle + 180).toFloat(),
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = 50,
                easing = FastOutSlowInEasing
            )
        )

        Image(
            imageVector =  loadXmlPicture("runway"),
            contentDescription = "runway",
            alpha = 0.65f,
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(animatedAngle.value)
                .size(min(maxWidth, maxHeight))
        )
    }
}