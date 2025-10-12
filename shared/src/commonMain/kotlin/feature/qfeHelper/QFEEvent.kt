package feature.qfeHelper

sealed class QFEEvent {
    data class SubmitICAO(val icao: String) : QFEEvent()
    data class SubmitElevationMeters(val elevation: Int) : QFEEvent()
    data class SubmitQFEmmHg(val mmHg: Int) : QFEEvent()
    data class SubmitHeightPlusMeters(val meters: Int) : QFEEvent()
    data class SubmitTemperature(val celsius: Int) : QFEEvent()
}