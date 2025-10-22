package preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import feature.airportsBase.ui.CollapsedAirport
import services.airportService.model.Airport
import services.airportService.model.enums.AirportType

@Preview
@Composable
fun CollapsedAirportPreview() {
    CollapsedAirport(
        Airport(
            icao = "UUWW",
            type = AirportType.LARGE,
            name = "Vnukovo International airport",
            latitude = 0f,
            longitude = 0f,
            elevation = 0,
        )
    ) {}
}
