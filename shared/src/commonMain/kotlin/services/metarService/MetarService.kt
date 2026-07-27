package services.metarService

import services.commonApi.Result

interface MetarService {
    suspend fun getMetar(station: String): Result<String>
    suspend fun getTaf(station: String): Result<String>
}