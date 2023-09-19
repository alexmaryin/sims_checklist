package feature.metarscreen.ui.windSegment

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import feature.metarscreen.model.RunwayUi
import ui.loadXmlPicture
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Runway(boxScope: BoxWithConstraintsScope, data: RunwayUi, userAngleEnter: (Int) -> Unit) {

    var centerX by remember { mutableStateOf(0f) }
    var centerY by remember { mutableStateOf(0f) }
    var pointerX by remember { mutableStateOf(0f) }
    var pointerY by remember { mutableStateOf(0f) }

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
            .onGloballyPositioned {
                val windowBounds = it.boundsInWindow()
                centerX = windowBounds.size.width / 2f
                centerY = windowBounds.size.height / 2f
            }
            .onPointerEvent(PointerEventType.Move) { event ->
                val pointer = event.changes.last()
                if (pointer.pressed) {
                    pointerX = pointer.position.x
                    pointerY = pointer.position.y
                    var angle =  (-atan2(pointerY - centerY, pointerX - centerX) * (180f / PI)).roundToInt()
                    userAngleEnter(angle)
                }
            }
            .drawWithContent {
                drawLine(Color.Red, Offset(centerX, centerY), Offset(pointerX, pointerY))
            }
    ) {
        val filename = if(isSystemInDarkTheme()) "runway_dark" else "runway_light"
        Image(
            imageVector = loadXmlPicture(filename),
            contentDescription = "runway",
            modifier = Modifier
                .size(min(boxScope.maxWidth, boxScope.maxHeight))
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
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )

        Text(
            text = data.high,
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .rotate(180f)
                .align(Alignment.TopCenter)
        )
    }
}
