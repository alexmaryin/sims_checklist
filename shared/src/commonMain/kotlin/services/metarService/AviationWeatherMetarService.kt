package services.metarService

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import services.commonApi.ErrorType
import services.commonApi.Result
import utils.isNetworkConnected

class AviationWeatherMetarService(
    private val client: HttpClient
) : MetarService {
    override suspend fun getMetar(station: String): Result<String> {
        if (!isNetworkConnected()) {
            return Result.Error(ErrorType.NO_CONNECTION, "No internet connection")
        }

        return try {
            val response = client.get("${HttpRoutes.AVIATION_WEATHER_METAR}?ids=$station&format=raw")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val text = response.bodyAsText().trim()
                    if (text.isEmpty()) {
                        Result.Error(ErrorType.EMPTY_RESULT, "No METAR data for station $station")
                    } else {
                        Result.Success(text)
                    }
                }
                HttpStatusCode.NoContent -> Result.Error(ErrorType.EMPTY_RESULT, "No METAR data for station $station")
                HttpStatusCode.BadRequest -> Result.Error(ErrorType.BAD_REQUEST, response.bodyAsText())
                else -> Result.Error(ErrorType.UNKNOWN, "HTTP ${response.status}")
            }
        } catch (e: Exception) {
            Result.Error(ErrorType.UNKNOWN, "Failed to fetch METAR: ${e.message}")
        }
    }

    override suspend fun getTaf(station: String): Result<String> {
        if (!isNetworkConnected()) {
            return Result.Error(ErrorType.NO_CONNECTION, "No internet connection")
        }

        return try {
            val response = client.get("${HttpRoutes.AVIATION_WEATHER_TAF}?ids=$station&format=raw")
            when (response.status) {
                HttpStatusCode.OK -> Result.Success(response.bodyAsText().trim())
                HttpStatusCode.NoContent -> Result.Success("")
                else -> Result.Success("")
            }
        } catch (_: Exception) {
            Result.Success("")
        }
    }
}
