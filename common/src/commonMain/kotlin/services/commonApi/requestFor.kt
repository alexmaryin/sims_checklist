package services.commonApi

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import utils.isNetworkConnected

suspend inline fun <reified R> HttpClient.requestFor(url: String): Result<R> =
    if (isNetworkConnected()) try {
        Result.Success(get(url))
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
    } catch (e: Exception) {
        // Other errors
        Result.Error(ErrorType.UNKNOWN, "Unknown error")
    }
    else {
        Result.Error(ErrorType.NO_CONNECTION, "No internet connection")
    }