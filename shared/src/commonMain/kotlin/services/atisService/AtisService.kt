package services.atisService

import services.atisService.model.DatisEntry
import services.commonApi.Result

interface AtisService {
    suspend fun getDatis(icao: String): Result<List<DatisEntry>>
}
