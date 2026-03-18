package feature.coldTemperature

sealed class ColdTemperatureEvent {
    data class SubmitICAO(val icao: String) : ColdTemperatureEvent()
    data class SubmitTemperature(val celsius: Int) : ColdTemperatureEvent()
    data class SubmitAirportElevation(val feet: Int) : ColdTemperatureEvent()
    data class AddWaypoint(val name: String, val altitudeFeet: Int) : ColdTemperatureEvent()
    data object ClearError : ColdTemperatureEvent()
}
