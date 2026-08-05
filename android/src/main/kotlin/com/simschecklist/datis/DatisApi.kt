package com.simschecklist.datis

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object DatisApi {
    private const val BASE_URL = "https://datis.clowd.io/api"
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getStations(): List<String> {
        val body = call("stations")
        return json.decodeFromString<List<String>>(body)
    }

    suspend fun getAll(): List<DatisResponse> {
        val body = call("all")
        return json.decodeFromString<List<DatisResponse>>(body)
    }

    suspend fun getAirport(airport: String): DatisResponse {
        val body = call(airport)
        return json.decodeFromString<DatisResponse>(body)
    }

    private suspend fun call(endpoint: String): String = suspendCancellableCoroutine { cont ->
        try {
            val url = URL("$BASE_URL/$endpoint")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 8000
            connection.readTimeout = 8000

            val responseCode = connection.responseCode
            val body = try {
                connection.inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
            }

            if (responseCode == 200) {
                cont.resume(body)
            } else if (responseCode == 404 || responseCode == 400) {
                try {
                    val error = json.decodeFromString<ErrorResponse>(body)
                    cont.resumeWithException(Exception(error.error))
                } catch (e: Exception) {
                    cont.resumeWithException(Exception("HTTP $responseCode"))
                }
            } else {
                cont.resumeWithException(Exception("HTTP $responseCode"))
            }
        } catch (e: Exception) {
            cont.resumeWithException(e)
        }
    }
}

@Serializable
data class DatisResponse(
    val airport: String,
    val text: String,
    val wind: String? = null,
    val vis: String? = null,
    val altimeter: String? = null,
    val temperature: String? = null,
    val dewpoint: String? = null,
    val runway: String? = null,
    val remarks: String? = null
)

@Serializable
data class ErrorResponse(
    @SerialName("error") val error: String
)
