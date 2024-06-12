package di

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module
import services.airportService.AirportService
import services.airportService.LocalBaseConverter
import services.airportService.localService.AirportServiceRealmImpl
import services.airportService.localService.RealmConverter
import services.airportService.model.realm.AirportRealm
import services.airportService.model.realm.FrequencyRealm
import services.airportService.model.realm.HistoryAirportRealm
import services.airportService.model.realm.MetadataRealm
import services.airportService.model.realm.RunwayRealm

expect fun getRealmDirectory(): String

val realmModule = module {

    val config = RealmConfiguration.Builder(
        setOf(
            FrequencyRealm::class,
            RunwayRealm::class,
            AirportRealm::class,
            MetadataRealm::class,
            HistoryAirportRealm::class
        )
    ).directory(getRealmDirectory())
        .build()
    val realm = try {
        Realm.open(config)
    } catch (e: RuntimeException) {
        println("Error: ${e.localizedMessage}")
        throw (e)
    }

    single<AirportService> { AirportServiceRealmImpl(realm) }
    single<LocalBaseConverter> { RealmConverter(realm) }
}