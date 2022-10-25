package di

import services.metarService.MetarService
import services.metarService.MetarServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import org.koin.dsl.module
import services.airportService.AirportService
import services.airportService.AirportServiceImpl
import services.metarService.CheckWxMetarService

val apiModule = module {

    val httpClient = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

//    single<MetarService> { MetarServiceImpl(httpClient) }
    single<MetarService> { CheckWxMetarService(httpClient) }
    single<AirportService> { AirportServiceImpl(httpClient) }
}