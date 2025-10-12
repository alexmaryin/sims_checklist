package feature.airportsBase.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import services.airportService.model.Airport

@Composable
fun CollapsedAirport(airport: Airport, onClick: () -> Unit, onMetarClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        AirportHeader(
            modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(onClick = onClick),
            icao = airport.icao,
            name = airport.name,
            onMetarClick = onMetarClick
        )
    }
}