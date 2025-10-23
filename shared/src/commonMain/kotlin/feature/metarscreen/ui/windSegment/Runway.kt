package feature.metarscreen.ui.windSegment

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import feature.metarscreen.model.RunwayUi
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.runway_dark
import sims_checklist.shared.generated.resources.runway_light
import kotlin.math.roundToInt

expect fun runwayScrollOrientation(): Orientation
expect fun deltaScroll(delta: Float): Float

@Composable
fun BoxWithConstraintsScope.Runway(data: RunwayUi, userAngleEnter: (Int) -> Unit) {

    var offset by remember { mutableFloatStateOf(data.lowHeading.toFloat()) }

    val animatedAngle = animateFloatAsState(
        targetValue = data.lowHeading.toFloat(),
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 50,
            easing = FastOutSlowInEasing
        )
    )
    Box(
        modifier = Modifier
            .padding(5.dp)
            .rotate(animatedAngle.value)
            .clipToBounds()
            .scrollable(orientation = runwayScrollOrientation(),
                state = rememberScrollableState { delta ->
                    offset = (offset + deltaScroll(delta)).coerceIn(1f..180f)
                    userAngleEnter (offset.roundToInt())
                    delta
                })
    ) {
        val runwayResource = if(isSystemInDarkTheme()) Res.drawable.runway_dark else Res.drawable.runway_light
        Image(
            painter = painterResource(runwayResource),
            contentDescription = "runway",
            modifier = Modifier
                .size(min(this@Runway.maxWidth, this@Runway.maxHeight))
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    val gradientHeight = size.height / 5
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0.3f to Color.Transparent, 0.8f to Color.Black,
                            startY = 0f, endY = gradientHeight
                        ),
                        blendMode = BlendMode.DstIn
                    )
                    drawRect(
                        brush = Brush.verticalGradient(
                            0.3f to Color.Black, 0.7f to Color.Transparent,
                            startY = size.height - gradientHeight,
                            endY = size.height
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        )

        Text(
            text = data.low,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )

        Text(
            text = data.high,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .rotate(180f)
                .align(Alignment.TopCenter)
        )
    }
}
