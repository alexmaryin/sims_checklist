package di

import services.metarService.MetarService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import services.airportService.updateService.AirportUpdateService
import services.airportService.updateService.AirportUpdateServiceImpl
import services.atisService.AtisInfoService
import services.atisService.AtisService
import services.metarService.AviationWeatherMetarService

val apiModule = module {

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR DEBUG: $message")
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 15000
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            retryOnException(maxRetries = 3, retryOnTimeout = true)
            exponentialDelay()
        }
    }

    single<MetarService> { AviationWeatherMetarService(httpClient) }
    single<AirportUpdateService> { AirportUpdateServiceImpl(httpClient) }
    single<AtisService> { AtisInfoService(httpClient) }
}