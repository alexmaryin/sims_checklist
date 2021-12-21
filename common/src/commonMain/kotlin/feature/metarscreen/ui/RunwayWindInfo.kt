package feature.metarscreen.ui

import androidx.compose.runtime.Composable
import feature.metarscreen.model.Wind
import feature.metarscreen.ui.airportSegment.RunwayHeadwindBubble
import feature.metarscreen.ui.airportSegment.RunwayTailwindBubble

@Composable
fun RunwayWindInfo(runwayName: String, wind: Wind) {
    when(wind) {
        is Wind.LeftCrossHeadWind, is Wind.RightCrossHeadWind -> RunwayHeadwindBubble(runwayName, wind)
        is Wind.LeftCrossTailWind, is Wind.RightCrossTailWind -> RunwayTailwindBubble(runwayName, wind)
    }
}