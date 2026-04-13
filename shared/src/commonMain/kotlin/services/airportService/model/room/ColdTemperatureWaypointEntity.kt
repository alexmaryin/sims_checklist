package services.airportService.model.room

import androidx.room.Entity

@Entity(
    tableName = "cold_temperature_waypoints",
    primaryKeys = ["icao", "position"]
)
data class ColdTemperatureWaypointEntity(
    val icao: String,
    val position: Int,
    val name: String,
    val altitudeFeet: Int
)
