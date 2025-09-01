package services.airportService.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airports")
data class AirportEntity(
    @PrimaryKey val icao: String,
    val type: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val elevation: Int,
    val webSite: String? = null,
    val wiki: String? = null
)