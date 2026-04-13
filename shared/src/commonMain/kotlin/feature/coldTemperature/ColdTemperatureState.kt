package feature.coldTemperature

import alexmaryin.metarkt.helpers.calculateColdTemperatureCorrections
import feature.coldTemperature.ui.model.ColdTemperatureWaypoint
import kotlinx.serialization.Serializable
import services.coldTemperature.WaypointSegment
import services.coldTemperature.determineSegment

@Serializable
data class ColdTemperatureState(
    val airportICAO: String? = null,
    val airportName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val temperatureCelsius: Int = -20,
    val airportElevationFeet: Int = 300,
    val fafAltitudeFeet: Int = 3300,
    val mdaAltitudeFeet: Int = 500,
    val waypoints: List<ColdTemperatureWaypoint> = emptyList()
)

fun List<ColdTemperatureWaypoint>.recalculate(
    airportElevation: Int,
    temperatureCelsius: Int,
    fafAltitude: Int,
    mdaAltitude: Int
): List<ColdTemperatureWaypoint> {
    val corrections = calculateColdTemperatureCorrections(
        fafAltitude = fafAltitude,
        mdaAltitude = mdaAltitude,
        airportElevation = airportElevation,
        reportedTemperatureC = temperatureCelsius
    )

    val correctedWaypoints = map { waypoint ->
        val segment = determineSegment(waypoint.altitudeFeet, fafAltitude)
        val correction = when (segment) {
            WaypointSegment.ABOVE_FAF -> corrections.intermediateSegmentCorrection
            WaypointSegment.BELOW_FAF -> corrections.finalSegmentCorrection
        }
        waypoint.copy(
            segment = segment,
            correctedAltitudeFeet = waypoint.altitudeFeet + correction
        )
    }.sortedWith(
        compareBy<ColdTemperatureWaypoint> {
            when (it.segment) {
                WaypointSegment.ABOVE_FAF -> 0
                WaypointSegment.BELOW_FAF -> 1
            }
        }.thenByDescending { it.altitudeFeet }
    )

    return correctedWaypoints
}
