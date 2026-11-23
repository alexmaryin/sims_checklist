package di

import androidx.room.Room
import androidx.room.RoomDatabase
import services.airportService.model.room.AirportDatabase
import services.storage.getAppDataDir
import java.io.File

actual fun getRoomBuilder(): RoomDatabase.Builder<AirportDatabase> {
    val dbFile = resolveAirportsDb(
        appRoot = getAppDataDir(),
        legacyDir = File(System.getProperty("user.home"), ".simschecklist")
    )
    return Room.databaseBuilder<AirportDatabase>(name = dbFile.absolutePath)
        .fallbackToDestructiveMigration(dropAllTables = false)
}
