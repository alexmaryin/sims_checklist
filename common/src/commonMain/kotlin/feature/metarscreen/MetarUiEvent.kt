package feature.metarscreen

import kotlinx.coroutines.CoroutineScope

sealed class MetarUiEvent {
    data class SubmitAngle(val new: Int) : MetarUiEvent()
    data class SubmitICAO(val station: String, val scope: CoroutineScope) : MetarUiEvent()
    data class SubmitRunwayHeading(val new: Int) : MetarUiEvent()
    object ShowInfoDialog : MetarUiEvent()
    object DismissInfoDialog : MetarUiEvent()
}
