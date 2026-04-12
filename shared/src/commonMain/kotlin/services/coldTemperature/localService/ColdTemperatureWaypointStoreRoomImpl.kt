package services.coldTemperature.localService

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import services.airportService.model.room.AirportDatabase
import services.airportService.model.room.toDomain
import services.coldTemperature.ApproachSegment
import services.coldTemperature.ColdTemperatureWaypointStore
import services.coldTemperature.StoredColdTemperatureWaypoint

class ColdTemperatureWaypointStoreRoomImpl(
    private val database: AirportDatabase
) : ColdTemperatureWaypointStore {

    override fun observeWaypointsByIcao(icao: String): Flow<List<StoredColdTemperatureWaypoint>> =
        database.coldTemperatureWaypointDao()
            .getWaypointsByIcao(icao)
            .map { waypoints -> waypoints.map { it.toDomain() } }

    override suspend fun addWaypoint(icao: String, name: String, altitudeFeet: Int, segment: ApproachSegment) =
        database.coldTemperatureWaypointDao().insertWaypoint(icao, name, altitudeFeet, segment.name)

    override suspend fun deleteWaypoint(icao: String, name: String) =
        database.coldTemperatureWaypointDao().deleteWaypoint(icao, name)
}
