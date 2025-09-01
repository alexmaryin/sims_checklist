package di

import androidx.room.Room
import androidx.room.RoomDatabase
import services.airportService.model.room.AirportDatabase
import java.io.File

actual fun getRoomBuilder(): RoomDatabase.Builder<AirportDatabase> {
    val appDir = File(System.getProperty("user.home"), ".simschecklist")
    if (!appDir.exists()) appDir.mkdirs()
    val dbName = File(appDir, "airports.db").absolutePath
    return Room.databaseBuilder(name = dbName)
}