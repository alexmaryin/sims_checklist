package services.metarService

import services.commonApi.Result
import services.metarService.model.MetarTaf

interface MetarService {
    suspend fun getMetar(station: String): Result<MetarTaf>
}