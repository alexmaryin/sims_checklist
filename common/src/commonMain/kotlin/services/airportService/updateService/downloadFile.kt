package services.airportService.updateService

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.flow
import java.io.File

suspend fun HttpClient.downloadFile(filename: String, url: String) = flow {
    println("Check if file $filename is already exist")
    if (File(filename).exists()) {
        println("Yes, it's here")
        emit(DownloadResult.Success)
    } else try {
        println("Downloading file...")
        val response = get<HttpResponse>(url)
        val data: ByteArray = response.receive()
        File(filename).writeBytes(data)
        println("File has been successfully saved")
        emit(DownloadResult.Success)
    } catch (e: TimeoutCancellationException) {
        emit(DownloadResult.Error("Connection timeout", e))
    } catch (e: Exception) {
        emit(DownloadResult.Error("Unexpected error", e))
    }
}