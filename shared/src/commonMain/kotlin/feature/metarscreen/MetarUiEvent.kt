package feature.metarscreen

import feature.metarscreen.model.RunwayUi
import kotlinx.coroutines.CoroutineScope

sealed class MetarUiEvent {
    data class SubmitAngle(val new: Int) : MetarUiEvent()
    data class SubmitICAO(val station: String, val scope: CoroutineScope) : MetarUiEvent()
    data class SubmitRunway(val new: RunwayUi) : MetarUiEvent()
    data object ShowInfoDialog : MetarUiEvent()
    data object DismissInfoDialog : MetarUiEvent()
}
