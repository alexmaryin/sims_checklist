package feature.metarscreen

import feature.metarscreen.model.RunwayUi
import kotlinx.coroutines.CoroutineScope

sealed class MetarUiEvent {
    data class SubmitWindAngle(val new: Int) : MetarUiEvent()
    data class SubmitWindSpeed(val new: Int) : MetarUiEvent()
    data class SubmitICAO(val station: String, val scope: CoroutineScope) : MetarUiEvent()
    data class SubmitRunway(val new: RunwayUi) : MetarUiEvent()
    data class SubmitRunwayAngle(val new: Int) : MetarUiEvent()
    data object ShowInfoDialog : MetarUiEvent()
    data object DismissInfoDialog : MetarUiEvent()
    data class LoadTopLatest(val scope: CoroutineScope) : MetarUiEvent()
}
