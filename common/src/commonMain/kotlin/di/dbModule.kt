package di

import database.AircraftBase
import database.AircraftBaseImpl
import org.koin.dsl.module
import repository.AircraftRepository
import repository.AircraftRepositoryImpl

val dbModule = module {
    single<AircraftBase> { AircraftBaseImpl() }
    single<AircraftRepository> { AircraftRepositoryImpl(get()) }
}