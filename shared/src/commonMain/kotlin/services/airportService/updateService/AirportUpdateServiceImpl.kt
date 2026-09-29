package services.airportService.updateService

import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import services.airportService.getFilePath
import services.airportService.updateService.AirportUpdateService.UpdateResult
import java.nio.file.Files
import kotlin.io.path.Path

class AirportUpdateServiceImpl(
    private val httpClient: HttpClient
) : AirportUpdateService {


    override suspend fun updateFlow(scope: CoroutineScope): Flow<UpdateResult> = channelFlow {
        send(UpdateResult.Progress("files...", 0))
        var hasErrors = false
        FilesLinks.files.forEachIndexed { idx, file ->
            scope.launch(Dispatchers.IO) {
                httpClient.downloadFile(getFilePath(file), "${FilesLinks.BASE_URL}/$file")
                    .collect { result ->
                        when (result) {
                            is DownloadResult.Error -> {
                                hasErrors = true
                                send(UpdateResult.Error(result.message, result.error))
                            }
                            DownloadResult.Success -> send(
                                UpdateResult.Progress(
                                    file,
                                    (((idx + 1).toFloat() / FilesLinks.files.size) * 100).toInt()
                                )
                            )
                        }
                    }
            }.join()
        }
        // Don't report Success if some files failed to download,
        // otherwise the converter will crash on missing files
        if (!hasErrors) send(UpdateResult.Success(System.currentTimeMillis()))
    }

    override suspend fun clearAfterUpdate() {
        FilesLinks.files.forEach { file ->
            Files.deleteIfExists(Path(getFilePath(file)))
            println("$file file deleted.")
        }
    }
}