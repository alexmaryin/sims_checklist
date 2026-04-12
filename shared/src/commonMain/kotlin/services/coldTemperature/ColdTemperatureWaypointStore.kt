package services.coldTemperature

import kotlinx.coroutines.flow.Flow

enum class ApproachSegment(val displayName: String) {
    INTERMEDIATE("Intermediate"),
    FINAL("Final"),
    MISSED_APPROACH("Missed Approach")
}

data class StoredColdTemperatureWaypoint(
    val name: String,
    val altitudeFeet: Int,
    val position: Int,
    val segment: ApproachSegment = ApproachSegment.INTERMEDIATE
)

interface ColdTemperatureWaypointStore {
    fun observeWaypointsByIcao(icao: String): Flow<List<StoredColdTemperatureWaypoint>>
    suspend fun addWaypoint(icao: String, name: String, altitudeFeet: Int, segment: ApproachSegment = ApproachSegment.INTERMEDIATE)
    suspend fun deleteWaypoint(icao: String, name: String)
}
