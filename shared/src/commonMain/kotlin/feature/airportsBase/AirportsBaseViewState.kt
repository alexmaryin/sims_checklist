package feature.airportsBase

import services.airportService.model.Airport

data class AirportsBaseViewState(
    val lastUpdate: String? = null,
    val airportsCount: Long = 0L,
    val updating: Boolean = false,
    val processingFile: String = "",
    val processingLabel: String = "",
    val progress: Int = 0,
    val searchString: String = "",
    val searchResult: List<Airport> = emptyList(),
    val selectedAirport: Airport? = null,
    val snackbar: AirportsSnackBarState? = null
)

sealed class AirportsSnackBarState(
    val message: String,
    val button: String,
    val event: AirportsUiEvent
) {
    data class ErrorHint(val error: String) : AirportsSnackBarState(
        message = error, button = "Close", event = AirportsUiEvent.SnackBarClose
    )
}
