package services.atisService

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import services.atisService.model.DatisEntry
import services.commonApi.ErrorType
import services.commonApi.Result
import services.metarService.HttpRoutes
import utils.isNetworkConnected

class AtisInfoService(
    private val client: HttpClient
) : AtisService {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getDatis(icao: String): Result<List<DatisEntry>> {
        if (!isNetworkConnected()) {
            return Result.Error(ErrorType.NO_CONNECTION, "No internet connection")
        }

        return try {
            val response = client.get("${HttpRoutes.ATIS_INFO}/$icao")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body = response.bodyAsText().trim()
                    parseDatisResponse(body, icao)
                }
                HttpStatusCode.NoContent ->
                    Result.Error(ErrorType.EMPTY_RESULT, "No D-ATIS data for station $icao")
                HttpStatusCode.BadRequest ->
                    Result.Error(ErrorType.BAD_REQUEST, response.bodyAsText())
                else ->
                    Result.Error(ErrorType.UNKNOWN, "HTTP ${response.status}")
            }
        } catch (e: Exception) {
            Result.Error(ErrorType.UNKNOWN, "Failed to fetch D-ATIS: ${e.message}")
        }
    }

    private fun parseDatisResponse(body: String, icao: String): Result<List<DatisEntry>> {
        return try {
            val entries = json.decodeFromString<List<DatisEntry>>(body)
            Result.Success(entries)
        } catch (e: kotlinx.serialization.SerializationException) {
            try {
                val errorObj = json.decodeFromString<AtisErrorResponse>(body)
                if (errorObj.error != null) {
                    Result.Error(ErrorType.EMPTY_RESULT, "No D-ATIS data for station $icao")
                } else {
                    Result.Error(ErrorType.UNKNOWN, "Unexpected response format")
                }
            } catch (_: Exception) {
                Result.Error(ErrorType.UNKNOWN, "Failed to parse D-ATIS response: ${e.message}")
            }
        }
    }
}

@Serializable
private data class AtisErrorResponse(
    val error: String? = null
)
