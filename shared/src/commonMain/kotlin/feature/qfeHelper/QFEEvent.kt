package feature.qfeHelper

import kotlinx.coroutines.CoroutineScope

sealed class QFEEvent {
    data class SubmitICAO(val icao: String, val scope: CoroutineScope) : QFEEvent()
    data class SubmitElevationMeters(val elevation: Int) : QFEEvent()
    data class SubmitQFEmmHg(val mmHg: Int) : QFEEvent()
    data class SubmitHeightPlusMeters(val meters: Int) : QFEEvent()
}