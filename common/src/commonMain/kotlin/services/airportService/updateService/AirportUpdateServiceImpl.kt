package services.airportService.updateService

import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import services.airportService.getFilePath
import services.airportService.updateService.AirportUpdateService.UpdateResult

class AirportUpdateServiceImpl(
    private val httpClient: HttpClient
) : AirportUpdateService {

    override suspend fun updateFlow(scope: CoroutineScope): Flow<UpdateResult> = channelFlow {
        FilesLinks.files.forEachIndexed { idx, file ->
            scope.launch(Dispatchers.IO) {
                httpClient.downloadFile(getFilePath(file), "${FilesLinks.BASE_URL}/$file")
                    .collect { result ->
                        when (result) {
                            is DownloadResult.Error -> send(UpdateResult.Error(result.message, result.error))
                            DownloadResult.Success -> send(
                                UpdateResult.Progress(
                                    file,
                                    (idx + 1) / FilesLinks.files.size * 100
                                )
                            )
                        }
                    }
            }.join()
        }
        send(UpdateResult.Success(System.currentTimeMillis()))
    }
}