package feature.remote.service

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import feature.metarscreen.model.MetarResponse

interface MetarService {

    suspend fun getMetar(station: String): MetarResponse

    companion object {
        fun create(): MetarService = MetarServiceImpl(
            HttpClient(CIO) {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            }
        )
    }

}