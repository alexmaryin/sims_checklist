package services.airportService.model.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "frequencies",
    foreignKeys = [ForeignKey(
        entity = AirportEntity::class,
        parentColumns = ["icao"],
        childColumns = ["airportIcao"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["airportIcao"])]
)
data class FrequencyEntity(
    @PrimaryKey val id: String,
    val airportIcao: String,
    val type: String,
    val description: String?,
    val valueMhz: Float
)