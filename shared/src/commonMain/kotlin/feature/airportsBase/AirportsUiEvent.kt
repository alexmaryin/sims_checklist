package feature.airportsBase

import kotlinx.coroutines.CoroutineScope

sealed class AirportsUiEvent {
    data object StartUpdate : AirportsUiEvent()
    data object SnackBarClose : AirportsUiEvent()
    data object Back : AirportsUiEvent()
    data object GetLastUpdate : AirportsUiEvent()
    data class SendSearch(val search: String) : AirportsUiEvent()
}
