package feature.metarscreen.ui

import androidx.compose.runtime.Composable
import feature.metarscreen.model.WindUi
import feature.metarscreen.ui.airportSegment.RunwayHeadwindBubble
import feature.metarscreen.ui.airportSegment.RunwayTailwindBubble

@Composable
fun RunwayWindInfo(runwayName: String, windUi: WindUi) {
    when(windUi) {
        is WindUi.LeftCrossHeadWindUi, is WindUi.RightCrossHeadWindUi -> RunwayHeadwindBubble(runwayName, windUi)
        is WindUi.LeftCrossTailWindUi, is WindUi.RightCrossTailWindUi -> RunwayTailwindBubble(runwayName, windUi)
    }
}