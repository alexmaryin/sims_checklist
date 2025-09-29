package di

import androidx.room.Room
import androidx.room.RoomDatabase
import ru.alexmaryin.simschecklist.AppAndroid
import services.airportService.model.room.AirportDatabase

actual fun getRoomBuilder(): RoomDatabase.Builder<AirportDatabase> {
    with(AppAndroid.instance()) {
        val dbFile = applicationContext.getDatabasePath("airports.db")
        return Room.databaseBuilder(context = applicationContext, name = dbFile.absolutePath)
    }
}