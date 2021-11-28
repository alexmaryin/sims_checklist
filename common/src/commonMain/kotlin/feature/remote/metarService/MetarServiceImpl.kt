package feature.remote.metarService

import feature.metarscreen.model.ErrorType
import feature.metarscreen.model.ErrorUi
import feature.metarscreen.model.MetarResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import utils.isNetworkConnected

class MetarServiceImpl(
    private val client: HttpClient
) : MetarService {
    override suspend fun getMetar(station: String): MetarResponse =
        if (isNetworkConnected()) try {
            MetarResponse.Success(
                client.get {
                    url("${HttpRoutes.METAR_TAF}/$station.json")
                })
        } catch (E: ClientRequestException) {
            // 4xx errors
            MetarResponse.Error(ErrorUi(ErrorType.CLIENT_ERROR, E.message))
        } catch (E: ServerResponseException) {
            // 5xx errors
            MetarResponse.Error(ErrorUi(ErrorType.SERVER_ERROR, E.message ?: "Server error"))
        } catch (E: Exception) {
            // Other errors, including no internet connection
            MetarResponse.Error(ErrorUi(ErrorType.UNKNOWN_ERROR, "Unknown error"))
        }
        else {
            MetarResponse.Error(ErrorUi(ErrorType.NO_CONNECTION_ERROR, "No internet connection"))
        }
}