package services.airportService.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metadata")
data class MetadataEntity(
    @PrimaryKey val id: String,
    val updateTimestamp: Long,
    val airportsCount: Long
)