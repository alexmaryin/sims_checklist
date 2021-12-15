package feature.metarscreen.ui.windSegment

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import feature.metarscreen.model.RunwayUi

@Composable
fun WindSegment(minSide: Dp, windAngle: Int, runway: RunwayUi) {
    BoxWithConstraints(
        modifier = Modifier.size(min(400.dp, minSide - 50.dp)),
    ) {
        CircleFace(boxScope = this, color = MaterialTheme.colors.onSurface)
        Runway(runway)
        WindPointer(boxScope = this, windAngle, color = MaterialTheme.colors.secondary)
    }
}