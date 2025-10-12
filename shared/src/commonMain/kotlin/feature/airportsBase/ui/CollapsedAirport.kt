package feature.airportsBase.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.airportsBase.AirportEventExecutor
import feature.airportsBase.AirportsUiEvent
import services.airportService.model.Airport

@Composable
fun CollapsedAirport(airport: Airport, eventsExecutor: AirportEventExecutor) {
    Card(
        Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        AirportHeader(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
                .clickable(onClick = { eventsExecutor(AirportsUiEvent.ExpandAirport(airport.icao))}),
            icao = airport.icao,
            name = airport.name,
            onMetarClick = { eventsExecutor(AirportsUiEvent.OpenAirportMetar(airport.icao)) },
            onQfeClick = { eventsExecutor(AirportsUiEvent.OpenQfeHelper(airport.icao)) }
        )
    }
}