package feature.metarscreen.ui.airportSegment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import services.airportService.model.Airport
import services.airportService.model.Runway
import ui.FlowRow
import ui.utils.RunwayTooltip

@Composable
fun AirportInfo(airport: Airport, onSelectRunway: (Runway) -> Unit) {

    var selectorRect by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(airport) {
        selectorRect = Rect.Zero
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = airport.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            RunwayTooltip {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Show notice about runway course",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box {

            FlowRow(spacing = 8.dp) {
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