package di

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module
import services.airportService.AirportService
import services.airportService.localService.AirportServiceRealmImpl
import services.airportService.model.realm.AirportRealm
import services.airportService.model.realm.FrequencyRealm
import services.airportService.model.realm.RunwayRealm

val realmModule = module {

    val config = RealmConfiguration.Builder(
        setOf(FrequencyRealm::class, RunwayRealm::class, AirportRealm::class)
    ).directory("./common/resources/realmdb")
        .build()
    val realm = try {
        Realm.open(config)
    } catch (e: RuntimeException) {
        println("Error: ${e.localizedMessage}")
        throw (e)
    }

    single<AirportService> { AirportServiceRealmImpl(realm) }
}