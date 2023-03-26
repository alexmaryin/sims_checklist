package services.airportService.updateService

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

fun interface AirportUpdateService {
    suspend fun updateFlow(scope: CoroutineScope): Flow<UpdateResult>

    sealed class UpdateResult {
        data class Success(val lastUpdate: Long) : UpdateResult()
        data class Progress(val file: String, val percent: Int) : UpdateResult()
        data class Error(val message: String, val e: Exception? = null) : UpdateResult()
    }
}