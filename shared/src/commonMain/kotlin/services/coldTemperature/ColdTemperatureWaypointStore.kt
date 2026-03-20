package services.coldTemperature

import kotlinx.coroutines.flow.Flow

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
