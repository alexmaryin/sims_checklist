package services.coldTemperature

import kotlinx.coroutines.flow.Flow

/**
 * Represents the segment type for cold temperature correction calculation.
 * Dynamically computed based on waypoint altitude relative to FAF and MDA altitudes.
 */
enum class WaypointSegment(val displayName: String) {
    ABOVE_FAF("Intermediate"),
    BELOW_FAF("Final")
}

data class StoredColdTemperatureWaypoint(
    val name: String,
    val altitudeFeet: Int,
    val position: Int
)

interface ColdTemperatureWaypointStore {
    fun observeWaypointsByIcao(icao: String): Flow<List<StoredColdTemperatureWaypoint>>
    suspend fun addWaypoint(icao: String, name: String, altitudeFeet: Int)
    suspend fun deleteWaypoint(icao: String, name: String)
}

/**
 * Dynamically determines the segment type based on waypoint altitude.
 * - ABOVE_FAF: altitude >= FAF altitude (similar to intermediate/missed approach)
 * - BELOW_FAF: altitude < FAF altitude (final approach segment)
 */
fun determineSegment(altitudeFeet: Int, fafAltitude: Int): WaypointSegment {
    return if (altitudeFeet >= fafAltitude) {
        WaypointSegment.ABOVE_FAF
    } else {
        WaypointSegment.BELOW_FAF
    }
}
