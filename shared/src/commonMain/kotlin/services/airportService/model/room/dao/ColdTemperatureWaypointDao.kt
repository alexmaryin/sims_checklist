package services.airportService.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import services.airportService.model.room.ColdTemperatureWaypointEntity

@Dao
interface ColdTemperatureWaypointDao {

    @Query("SELECT * FROM cold_temperature_waypoints WHERE icao = :icao ORDER BY position ASC")
    fun getWaypointsByIcao(icao: String): Flow<List<ColdTemperatureWaypointEntity>>

    @Query(
        """
        INSERT INTO cold_temperature_waypoints (icao, position, name, altitudeFeet)
        VALUES (
            :icao,
            COALESCE((SELECT MAX(position) + 1 FROM cold_temperature_waypoints WHERE icao = :icao), 0),
            :name,
            :altitudeFeet
        )
        """
    )
    suspend fun insertWaypoint(icao: String, name: String, altitudeFeet: Int)

    @Query("DELETE FROM cold_temperature_waypoints WHERE icao = :icao AND name = :name")
    suspend fun deleteWaypoint(icao: String, name: String)
}
