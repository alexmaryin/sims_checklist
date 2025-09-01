package services.airportService.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_airports")
data class HistoryAirportEntity(
    @PrimaryKey val timestamp: Long,
    val icao: String,
    val name: String
)