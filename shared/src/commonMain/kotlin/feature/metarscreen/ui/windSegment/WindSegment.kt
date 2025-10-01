package feature.metarscreen.ui.windSegment

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import feature.metarscreen.model.RunwayUi
import feature.metarscreen.model.WindComponent

@Composable
fun WindSegment(
    minSide: Dp,
    wind: WindComponent,
    runway: RunwayUi,
    userAngleEnter: (Int) -> Unit) {
    BoxWithConstraints(
        modifier = Modifier.size(min(400.dp, minSide)),
    ) {
        CircleFace(boxScope = this, color = MaterialTheme.colorScheme.onSurface)
        Runway(boxScope = this, runway, userAngleEnter)
        if (wind.speedKt > 0) {
            WindPointer(boxScope = this, wind.heading, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}