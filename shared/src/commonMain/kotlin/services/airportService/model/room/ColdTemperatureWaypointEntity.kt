package services.airportService.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "cold_temperature_waypoints",
    primaryKeys = ["icao", "position"]
)
data class ColdTemperatureWaypointEntity(
    val icao: String,
    val position: Int,
    val name: String,
    val altitudeFeet: Int,
    @ColumnInfo(defaultValue = "INTERMEDIATE")
    val segment: String = "INTERMEDIATE"
)
