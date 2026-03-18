package feature.coldTemperature

import alexmaryin.metarkt.helpers.coldTemperatureCorrectedAltitude

data class ColdTemperatureWaypoint(
    val number: Int,
    val name: String,
    val altitudeFeet: Int,
    val correctedAltitudeFeet: Int
)

data class ColdTemperatureState(
    val airportICAO: String? = null,
    val airportName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val temperatureCelsius: Int = 15,
    val airportElevationFeet: Int = 0,
    val waypoints: List<ColdTemperatureWaypoint> = emptyList()
) {
    val nextWaypointAltitudeFeet: Int
        get() = waypoints.lastOrNull()?.altitudeFeet ?: (airportElevationFeet + 3000)
}

fun List<ColdTemperatureWaypoint>.recalculate(
    airportElevationFeet: Int,
    temperatureCelsius: Int
): List<ColdTemperatureWaypoint> = mapIndexed { index, waypoint ->
    waypoint.copy(
        number = index + 1,
        correctedAltitudeFeet = correctedAltitudeFeet(
            altitudeFeet = waypoint.altitudeFeet,
            airportElevationFeet = airportElevationFeet,
            temperatureCelsius = temperatureCelsius
        )
    )
}

private fun correctedAltitudeFeet(
    altitudeFeet: Int,
    airportElevationFeet: Int,
    temperatureCelsius: Int
): Int {
    val heightAboveAirport = (altitudeFeet - airportElevationFeet).coerceAtLeast(0)
    return airportElevationFeet + coldTemperatureCorrectedAltitude(heightAboveAirport, temperatureCelsius)
}
