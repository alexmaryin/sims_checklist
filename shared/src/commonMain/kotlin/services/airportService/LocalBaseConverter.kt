package services.airportService

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface LocalBaseConverter {
    suspend fun convertFiles(scope: CoroutineScope): Flow<UpdateResult>

    sealed class UpdateResult {
        data class Progress(val label: String, val count: Long) : UpdateResult()
        data class Success(val count: Long) : UpdateResult()
    }
}