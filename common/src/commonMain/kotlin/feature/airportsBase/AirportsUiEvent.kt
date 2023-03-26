package feature.airportsBase

import kotlinx.coroutines.CoroutineScope

sealed class AirportsUiEvent {
    data class StartUpdate(val scope: CoroutineScope) : AirportsUiEvent()
    data class StartConvert(val scope: CoroutineScope) : AirportsUiEvent()
    object SnackBarClose : AirportsUiEvent()
    object Back : AirportsUiEvent()
    data class GetLastUpdate(val scope: CoroutineScope) : AirportsUiEvent()
}