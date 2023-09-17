package services.metarService

import io.ktor.client.*
import services.commonApi.Result
import services.commonApi.Result.Error
import services.commonApi.Result.Success
import services.commonApi.forError
import services.commonApi.forSuccess
import services.commonApi.requestFor
import services.metarService.model.MetarTaf
import services.metarService.model.WxMetar
import services.metarService.model.WxTaf
import common.BuildKonfig

class CheckWxMetarService(
    private val client: HttpClient
) : MetarService {
    override suspend fun getMetar(station: String): Result<MetarTaf> {
        val headers = mapOf("X-API-Key" to BuildKonfig.WXAPI_KEY)
        val metar = client.requestFor<WxMetar>(HttpRoutes.CHECK_WX_METAR + station, headers)
        val taf = client.requestFor<WxTaf>(HttpRoutes.CHECK_WX_TAF + station, headers)
        metar.forError { type, message ->
            return Error(type, message)
        }
        var metarString: String? = null
        var tafString: String? = null
        metar.forSuccess {
            if (it.results > 0) metarString = it.data.first()
        }
        taf.forSuccess {
            if (it.results > 0) tafString = it.data.first()
        }
        return Success(
            MetarTaf(
            icao = station,
            metar = metarString ?: "",
            taf = tafString ?: ""
        )
        )
    }
}