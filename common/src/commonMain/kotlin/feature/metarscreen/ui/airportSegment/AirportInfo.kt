package feature.metarscreen.ui.airportSegment

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import services.airportService.model.Airport
import ui.FlowRow

@Composable
fun AirportInfo(airport: Airport) {
    Column {
        Text(
            text = airport.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box {
            FlowRow(spacing = 8.dp) {
                airport.runways.forEachIndexed { index, runway ->
                    val selected = index == 0
                    RunwayChip("${runway.lowNumber}/${runway.highNumber}", selected) { rect ->
                        println("$rect")
                    }
                }
            }

        }


    }
}