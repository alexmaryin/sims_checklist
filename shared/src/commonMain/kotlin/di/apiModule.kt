package di

import services.metarService.MetarService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module
import services.airportService.updateService.AirportUpdateService
import services.airportService.updateService.AirportUpdateServiceImpl
import services.metarService.CheckWxMetarService

val apiModule = module {

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    single<MetarService> { CheckWxMetarService(httpClient) }
    single<AirportUpdateService> { AirportUpdateServiceImpl(httpClient) }
}