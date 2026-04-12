package feature.coldTemperature

import services.coldTemperature.ApproachSegment

sealed class ColdTemperatureEvent {
    data class SubmitICAO(val icao: String) : ColdTemperatureEvent()
    data class SubmitTemperature(val celsius: Int) : ColdTemperatureEvent()
    data class SubmitAirportElevation(val feet: Int) : ColdTemperatureEvent()
    data class SubmitFAFAltitude(val feet: Int) : ColdTemperatureEvent()
    data class SubmitMDAAltitude(val feet: Int) : ColdTemperatureEvent()
    data class AddWaypoint(val name: String, val altitudeFeet: Int, val segment: ApproachSegment = ApproachSegment.INTERMEDIATE) : ColdTemperatureEvent()
    data class DeleteWaypoint(val name: String) : ColdTemperatureEvent()
    data object ClearError : ColdTemperatureEvent()
}
