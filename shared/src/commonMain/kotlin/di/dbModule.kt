package di

import feature.mainScreen.database.AircraftBase
import feature.mainScreen.database.AircraftBaseImpl
import org.koin.dsl.module
import repository.AircraftRepository
import repository.AircraftRepositoryImpl

val dbModule = module {
    single<AircraftBase> { AircraftBaseImpl() }
    single<AircraftRepository> { AircraftRepositoryImpl(get()) }
}