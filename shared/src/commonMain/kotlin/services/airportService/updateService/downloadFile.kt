package services.airportService.updateService

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.flow
import java.io.File

fun HttpClient.downloadFile(filename: String, url: String) = flow {
    println("Check if file $filename is already exist")
    val target = File(filename)
    if (target.exists()) {
        println("Yes, it's here")
        emit(DownloadResult.Success)
    } else try {
        println("Downloading file...")
        val response: HttpResponse = get(url)
        if (!response.status.isSuccess()) {
            emit(DownloadResult.Error("Server responded ${response.status.value} for $url"))
            return@flow
        }
        // Stream to a temp file so a partial download never looks like a complete one
        val temp = File("$filename.tmp")
        try {
            temp.outputStream().use { out ->
                response.bodyAsChannel().toInputStream().use { it.copyTo(out, 8192) }
            }
            if (!temp.renameTo(target)) temp.copyTo(target, overwrite = true)
            println("File has been successfully saved")
            emit(DownloadResult.Success)
        } finally {
            temp.delete()
        }
    } catch (e: TimeoutCancellationException) {
        emit(DownloadResult.Error("Connection timeout", e))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        emit(DownloadResult.Error("Unexpected error", e))
    }
}
