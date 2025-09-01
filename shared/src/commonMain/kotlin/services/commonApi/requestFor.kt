package services.commonApi

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import utils.isNetworkConnected

suspend inline fun <reified R> HttpClient.requestFor(url: String, headers: Map<String, String> = emptyMap()): Result<R> =
    if (isNetworkConnected()) try {
        val response = get(url) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }
        Result.Success(response.body<R>())
    } catch (e: ClientRequestException) {
        // 4xx errors
        if (e.response.status == HttpStatusCode.BadRequest) {
            Result.Error(ErrorType.BAD_REQUEST, e.message)
        } else {
            Result.Error(ErrorType.OTHER_CLIENT_ERROR, e.message)
        }
    } catch (e: ServerResponseException) {
        // 5xx errors
        if (e.response.status == HttpStatusCode.ServiceUnavailable) {
            Result.Error(ErrorType.SERVER_UNAVAILABLE, e.message)
        } else {
            Result.Error(ErrorType.OTHER_SERVER_ERROR, e.message)
        }
    } catch (_: Exception) {
        // Other errors
        Result.Error(ErrorType.UNKNOWN, "Unknown error")
    }
    else {
        Result.Error(ErrorType.NO_CONNECTION, "No internet connection")
    }