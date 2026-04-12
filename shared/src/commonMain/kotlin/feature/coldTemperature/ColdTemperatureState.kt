package feature.coldTemperature

import alexmaryin.metarkt.helpers.calculateColdTemperatureCorrections
import feature.coldTemperature.ui.model.ColdTemperatureWaypoint
import kotlinx.serialization.Serializable
import services.coldTemperature.ApproachSegment

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
        val correction = when (waypoint.segment) {
            ApproachSegment.INTERMEDIATE,
            ApproachSegment.MISSED_APPROACH -> corrections.intermediateSegmentCorrection
            ApproachSegment.FINAL -> corrections.finalSegmentCorrection
        }
        waypoint.copy(correctedAltitudeFeet = waypoint.altitudeFeet + correction)
    }.sortedWith(
        compareBy<ColdTemperatureWaypoint> {
            when (it.segment) {
                ApproachSegment.INTERMEDIATE -> 0
                ApproachSegment.FINAL -> 1
                ApproachSegment.MISSED_APPROACH -> 2
            }
        }.thenByDescending { it.altitudeFeet }
    )

    return correctedWaypoints
}
