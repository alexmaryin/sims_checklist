package feature.metarscreen.ui.airportSegment

import androidx.compose.foundation.layout.*
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
import commonUi.ChipSelector
import commonUi.RunwayChip
import commonUi.utils.RunwayTooltip

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

        Box(modifier = Modifier.fillMaxWidth()) {

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                airport.runways.forEach { runway ->
                    RunwayChip(
                        text = "${runway.lowNumber}/${runway.highNumber}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    ) { rect ->
                        selectorRect = rect
                        onSelectRunway(runway)
                    }
                }
            }
            ChipSelector(selectorRect)
        }
    }
}