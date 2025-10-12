package preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import feature.airportsBase.ui.ExpandedAirport
import services.airportService.model.Airport
import services.airportService.model.Runway
import services.airportService.model.enums.AirportType
import services.airportService.model.enums.RunwaySurface

@Preview
@Composable
fun ExpandedAirportPreview() {
    ExpandedAirport(
        Airport(
            icao = "UUWW",
            type = AirportType.LARGE,
            name = "Vnukovo International airport",
            latitude = 55.5992f,
            longitude = 37.2731f,
            elevation = 685,
            webSite = "www.vnukovo.com",
            wiki = "www.wikipedia.com/vnukovo",
            runways = listOf(
                Runway(
                    lengthFeet = 8500,
                    widthFeet = 400,
                    surface = RunwaySurface.CONCRETE,
                    closed = true,
                    lowNumber = "02",
                    lowElevationFeet = 450,
                    lowHeading = 19,
                    highNumber = "20",
                    highElevationFeet = 433,
                    highHeading = 199
                )
            )
        ),
        onClick = {}
    ) {}
}