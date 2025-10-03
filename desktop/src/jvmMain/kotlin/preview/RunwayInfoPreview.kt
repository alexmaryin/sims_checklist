package preview

import RunwayInfo
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import services.airportService.model.Runway
import services.airportService.model.enums.RunwaySurface


@Preview
@Composable
fun RunwayInfoPreview() {
    RunwayInfo(
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
        ),
        Modifier.fillMaxWidth()
    )
}