package services.airportService.model.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "runways",
    foreignKeys = [ForeignKey(
        entity = AirportEntity::class,
        parentColumns = ["icao"],
        childColumns = ["airportIcao"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["airportIcao"])]
)
data class RunwayEntity(
    @PrimaryKey val id: String,
    val airportIcao: String,
    val lengthFeet: Int?,
    val widthFeet: Int?,
    val surface: String,
    val closed: Boolean,
    val lowNumber: String,
    val lowElevationFeet: Int?,
    val lowHeading: Int,
    val highNumber: String,
    val highElevationFeet: Int?,
    val highHeading: Int
)