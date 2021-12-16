package feature.metarscreen.ui.windSegment

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ui.loadXmlPicture

@Composable
fun Runway(data: RunwayUi) {

    val animatedAngle = animateFloatAsState(
        targetValue = data.angle.toFloat(),
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 50,
            easing = FastOutSlowInEasing
        )
    )

    BoxWithConstraints(
        modifier = Modifier
            .rotate(animatedAngle.value)
    ) {
        Image(
            imageVector = loadXmlPicture("runway"),
            contentDescription = "runway",
            modifier = Modifier
                .size(min(maxWidth, maxHeight))
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
                .padding(bottom = 5.dp)
        )

        Text(
            text = data.high,
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .rotate(180f)
                .align(Alignment.TopCenter)
                .padding(top = 5.dp)
        )
    }
}
