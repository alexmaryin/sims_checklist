package feature.airportsBase

import kotlinx.coroutines.CoroutineScope

sealed class AirportsUiEvent {
    data class StartUpdate(val scope: CoroutineScope) : AirportsUiEvent()
    data object SnackBarClose : AirportsUiEvent()
    data object Back : AirportsUiEvent()
    data class GetLastUpdate(val scope: CoroutineScope) : AirportsUiEvent()
}
