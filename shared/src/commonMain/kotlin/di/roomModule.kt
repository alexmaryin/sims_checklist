package di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import services.airportService.AirportService
import services.airportService.LocalBaseConverter
import services.airportService.localService.AirportServiceRoomImpl
import services.airportService.localService.RoomConverter
import services.airportService.model.room.AirportDatabase

expect fun getRoomBuilder(): RoomDatabase.Builder<AirportDatabase>

val roomModule = module {

    fun getRoomDb(builder: RoomDatabase.Builder<AirportDatabase>) : AirportDatabase {
        return builder
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    val roomDb = getRoomDb(getRoomBuilder())

    single<AirportService> { AirportServiceRoomImpl(roomDb) }
    single<LocalBaseConverter> { RoomConverter(roomDb) }
}