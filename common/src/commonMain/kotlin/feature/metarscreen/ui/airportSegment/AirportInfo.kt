package feature.metarscreen.ui.airportSegment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import services.airportService.model.Airport
import services.airportService.model.Runway
import ui.FlowRow

@Composable
fun AirportInfo(airport: Airport, onSelectRunway: (Runway) -> Unit) {
    Column {
        Text(
            text = airport.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box {

            var selectorRect by remember { mutableStateOf(Rect.Zero) }

            FlowRow(
                spacing = 8.dp
            ) {
                airport.runways.forEach { runway ->
                    RunwayChip("${runway.lowNumber}/${runway.highNumber}") { rect ->
                        selectorRect = rect
                        onSelectRunway(runway)
                    }
                }
            }
            ChipSelector(selectorRect)
        }
    }
}