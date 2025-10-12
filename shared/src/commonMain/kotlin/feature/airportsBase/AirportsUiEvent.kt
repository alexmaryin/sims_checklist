package feature.airportsBase

sealed class AirportsUiEvent {
    data object StartUpdate : AirportsUiEvent()
    data object SnackBarClose : AirportsUiEvent()
    data object Back : AirportsUiEvent()
    data object GetLastUpdate : AirportsUiEvent()
    data class SendSearch(val search: String) : AirportsUiEvent()
    data class ExpandAirport(val icao: String) : AirportsUiEvent()
    data class OpenAirportMetar(val icao: String) : AirportsUiEvent()
    data class OpenQfeHelper(val icao: String) : AirportsUiEvent()
}
