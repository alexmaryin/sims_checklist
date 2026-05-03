package services.airportService.model.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.AutoMigration
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import services.airportService.model.room.dao.AirportDao
import services.airportService.model.room.dao.ColdTemperatureWaypointDao
import services.airportService.model.room.dao.HistoryAirportDao
import services.airportService.model.room.dao.MetadataDao

@Database(
    entities = [
        AirportEntity::class,
        FrequencyEntity::class,
        RunwayEntity::class,
        HistoryAirportEntity::class,
        MetadataEntity::class,
        ColdTemperatureWaypointEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
@ConstructedBy(AirportDatabaseConstructor::class)
abstract class AirportDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao
    abstract fun historyDao(): HistoryAirportDao
    abstract fun metadataDao(): MetadataDao
    abstract fun coldTemperatureWaypointDao(): ColdTemperatureWaypointDao
}


@Suppress("KotlinNoActualForExpect")
expect object AirportDatabaseConstructor : RoomDatabaseConstructor<AirportDatabase> {
    override fun initialize(): AirportDatabase
}
