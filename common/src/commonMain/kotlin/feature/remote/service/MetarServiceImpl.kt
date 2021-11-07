package feature.remote.service

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import feature.metarscreen.model.MetarResponse
import feature.remote.service.HttpRoutes
import feature.remote.service.MetarService

class MetarServiceImpl(
    private val client: HttpClient
) : MetarService {
    override suspend fun getMetar(station: String): MetarResponse =
        try {
            MetarResponse.Success(
                client.get {
                    url("${HttpRoutes.METAR_TAF}/$station.json")
                })
        } catch (E: ClientRequestException) {
            // 4xx errors
            MetarResponse.Error(E)
        } catch (E:ServerResponseException) {
            // 5xx errors
            MetarResponse.Error(E)
        }
}