package feature.metarscreen.ui.windSegment

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import feature.metarscreen.model.RunwayUi
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val CIRCLE_PADDING = 50.dp
val LONG_STROKE = 20.dp
val SHORT_STROKE = 10.dp

@Composable
fun BoxWithConstraintsScope.CircleFaceWithRunway(
    runway: RunwayUi,
    userAngleEnter: (Int) -> Unit,
    color: Color,
    surface: Color = MaterialTheme.colorScheme.surfaceDim
) {
    apply {
        val sizeDp = min(maxWidth, maxHeight)
        val radius = (sizeDp - CIRCLE_PADDING) / 2
        val center = sizeDp / 2

        Canvas(modifier = Modifier.size(min(maxWidth, maxHeight)).align(Alignment.Center)) {
            drawCircle(
                color = surface,
                radius = (radius + CIRCLE_PADDING / 2).toPx(),
                center = Offset(center.toPx(), center.toPx()),
                style = Fill
            )
            drawCircle(
                color = color,
                radius = radius.toPx(),
                center = Offset(center.toPx(), center.toPx()),
                style = Stroke(2.dp.toPx())
            )
            for (angle in 0..360 step 5) {
                // strokes for degrees
                val radians = angle * PI / 180
                val startRadius = radius - if (angle % 10 == 0) LONG_STROKE else SHORT_STROKE
                val xCos = cos((radians).toFloat())
                val ySin = sin((radians).toFloat())
                val startX = center.toPx() + startRadius.toPx() * xCos
                val startY = center.toPx() + startRadius.toPx() * ySin
                val x = center.toPx() + radius.toPx() * xCos
                val y = center.toPx() + radius.toPx() * ySin
                drawLine(
                    color = color,
                    start = Offset(startX, startY),
                    end = Offset(x, y),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }

        Runway(runway, userAngleEnter)

        DegreesLabels(radius, color)
    }
}

@Composable
fun BoxWithConstraintsScope.DegreesLabels(radius: Dp, color: Color) {
    apply {
        for (angle in 30..360 step 30) {
            Text(
                text = "$angle",
                modifier = Modifier
                    .align(Alignment.Center)
                    .rotate(angle.toFloat())
                    .offset(0.dp, 30.dp - radius),
                style = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                ),
                color = color
            )
        }
    }
}
