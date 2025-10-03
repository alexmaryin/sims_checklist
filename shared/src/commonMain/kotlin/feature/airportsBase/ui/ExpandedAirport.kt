package feature.airportsBase.ui

import RunwayInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import services.airportService.model.Airport
import ui.CaptionedDivider
import ui.LinkText
import ui.utils.largeWithShadow
import kotlin.math.abs

@Composable
fun ExpandedAirport(airport: Airport, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(onClick = onClick),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = airport.icao, modifier = Modifier.requiredWidth(90.dp), style = largeWithShadow())
                Text(text = airport.name)
            }
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
                CaptionedDivider("Runways:")
                airport.runways.forEach { RunwayInfo(it, Modifier.fillMaxWidth()) }
            }
            if (airport.frequencies.isNotEmpty()) {
                CaptionedDivider("Frequencies:")
                airport.frequencies.forEach { FrequencyInfo(it, Modifier.fillMaxWidth()) }
            }
        }
    }
}

/**
 * Converts a decimal degree value into a Degrees, Minutes, Seconds (DMS) string.
 *
 * @param isLatitude `true` if the coordinate is latitude, `false` for longitude.
 * This determines the cardinal direction (N/S for latitude, E/W for longitude).
 * @return A formatted DMS string, e.g., "55° 35' 57.12" N".
 */
private fun Float.toDMS(isLatitude: Boolean): String {
    val absolute = abs(this)
    val degrees = absolute.toInt()
    val minutesNotTruncated = (absolute - degrees) * 60
    val minutes = minutesNotTruncated.toInt()
    val seconds = (minutesNotTruncated - minutes) * 60

    val direction = if (isLatitude) {
        if (this >= 0) "N" else "S"
    } else {
        if (this >= 0) "E" else "W"
    }

    return "%d°%d'%.2f\"%s".format(degrees, minutes, seconds, direction)
}