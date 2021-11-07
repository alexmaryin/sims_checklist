package ui.metarscreen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import decompose.MetarScanner
import feature.metarscreen.WindViewState
import ui.loadXmlPicture

val ARROW_PADDING = CIRCLE_PADDING + LONG_STROKE + 60.dp

@Composable
fun WindPointer(boxScope: BoxWithConstraintsScope, component: MetarScanner, color: Color) {
    boxScope.apply {

        val state: WindViewState by component.state.subscribeAsState()

        val angle = animateFloatAsState(
            targetValue = with(state.data.metarAngle ?: state.data.userAngle) {
                if (this >= 180) this - 180 else this + 180
            }.toFloat(),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        Image(
            imageVector =  loadXmlPicture("ic_wind_vane.xml"),
            contentDescription = "Wind vane",
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(angle.value)
                .size(min(maxWidth, maxHeight) - ARROW_PADDING),
            colorFilter = ColorFilter.tint(color)
        )
    }
}
