package feature.metarscreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import services.airportService.model.Airport

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
        Row {
            airport.runways.forEach { runway ->
                Text("${runway.lowNumber}/${runway.highNumber}")
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}