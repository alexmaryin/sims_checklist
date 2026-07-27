package services.metarService

import io.ktor.client.*
import services.commonApi.Result
import services.commonApi.ErrorType
import services.commonApi.requestFor
import services.metarService.model.MetarTaf

class MetarServiceImpl(
    private val client: HttpClient
) : MetarService {
    private var cachedMetarTaf: MetarTaf? = null

    override suspend fun getMetar(station: String): Result<String> {
        val response = client.requestFor<MetarTaf>("${HttpRoutes.METAR_TAF}/$station.json")
        return when (response) {
            is Result.Success -> {
                cachedMetarTaf = response.value
                if (response.value.metar.isNotEmpty()) {
                    Result.Success(response.value.metar)
                } else {
                    Result.Error(ErrorType.EMPTY_RESULT, "No METAR data for station $station")
                }
            }
            is Result.Error -> response
        }
    }

    override suspend fun getTaf(station: String): Result<String> {
        val cached = cachedMetarTaf
        return if (cached != null && cached.icao == station) {
            Result.Success(cached.taf)
        } else {
            val response = client.requestFor<MetarTaf>("${HttpRoutes.METAR_TAF}/$station.json")
            when (response) {
                is Result.Success -> {
                    cachedMetarTaf = response.value
                    Result.Success(response.value.taf)
                }
                is Result.Error -> Result.Success("")
            }
        }
    }
}