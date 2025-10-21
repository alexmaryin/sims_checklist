package feature.airportsBase.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.airportsBase.AirportEventExecutor
import feature.airportsBase.AirportsUiEvent
import services.airportService.model.Airport
import commonUi.CaptionedDivider
import commonUi.LinkText
import commonUi.utils.RunwayTooltip
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.info
import utils.toDMS

@Composable
fun ExpandedAirport(
    airport: Airport,
    eventsExecutor: AirportEventExecutor
) {
    Card(
        Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AirportHeader(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
                    .clickable(onClick = { eventsExecutor(AirportsUiEvent.ExpandAirport(airport.icao)) }),
                icao = airport.icao,
                name = airport.name,
                onMetarClick = { eventsExecutor(AirportsUiEvent.OpenAirportMetar(airport.icao)) },
                onQfeClick = { eventsExecutor(AirportsUiEvent.OpenQfeHelper(airport.icao)) }
            )
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "elevation: ${airport.elevation} ft")
                FlowRow(
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val latitudeStr = airport.latitude.toDMS(true)
                    val longtitudeStr = airport.longitude.toDMS(false)
                    Text(text = "LAT: $latitudeStr")
                    Text(text = "LON: $longtitudeStr")
                }
            }
            airport.webSite?.let {
                LinkText(
                    text = "Airport website: $it",
                    url = it,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            }
            airport.wiki?.let {
                LinkText(
                    text = "Airport wiki: $it",
                    url = it,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            }
            if (airport.runways.isNotEmpty()) {
                CaptionedDivider {
                    Text("Runways: ")
                    RunwayTooltip {
                        Icon(
                            painter = painterResource(Res.drawable.info),
                            contentDescription = "Show notice about runway course"
                        )
                    }
                }
                airport.runways.forEach { RunwayInfo(it, Modifier.fillMaxWidth()) }
            }
            if (airport.frequencies.isNotEmpty()) {
                CaptionedDivider { Text("Frequencies: ") }
                airport.frequencies.forEach { FrequencyInfo(it, Modifier.fillMaxWidth()) }
            }
        }
    }
}