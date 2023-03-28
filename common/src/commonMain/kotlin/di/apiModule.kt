package di

import services.metarService.MetarService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import org.koin.dsl.module
import services.airportService.updateService.AirportUpdateService
import services.airportService.updateService.AirportUpdateServiceImpl
import services.metarService.CheckWxMetarService

val apiModule = module {

    val httpClient = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.INFO
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    single<MetarService> { CheckWxMetarService(httpClient) }
    single<AirportUpdateService> { AirportUpdateServiceImpl(httpClient) }
}