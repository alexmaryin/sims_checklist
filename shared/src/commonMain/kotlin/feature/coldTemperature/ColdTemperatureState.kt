package feature.coldTemperature

import alexmaryin.metarkt.helpers.coldTemperatureCorrectedAltitude
import feature.coldTemperature.ui.model.ColdTemperatureWaypoint
import kotlinx.serialization.Serializable

@Serializable
data class ColdTemperatureState(
    val airportICAO: String? = null,
    val airportName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val temperatureCelsius: Int = -20,
    val airportElevationFeet: Int = 300,
    val waypoints: List<ColdTemperatureWaypoint> = emptyList()
)

fun List<ColdTemperatureWaypoint>.recalculate(
    airportElevation: Int,
    temperatureCelsius: Int
): List<ColdTemperatureWaypoint> = map { waypoint ->
    val waypointAGL = (waypoint.altitudeFeet - airportElevation).coerceAtLeast(0)
    waypoint.copy(
        correctedAltitudeFeet = airportElevation + coldTemperatureCorrectedAltitude(
            waypointAGL,
            temperatureCelsius
        ),
    )
}
