package services.metarService

import io.ktor.client.*
import services.commonApi.requestFor
import services.metarService.model.MetarTaf

class MetarServiceImpl(
    private val client: HttpClient
) : MetarService {
    override suspend fun getMetar(station: String) =
        client.requestFor<MetarTaf>("${HttpRoutes.METAR_TAF}/$station.json")
}