package services.metarService

import io.ktor.client.*
import services.commonApi.Result
import services.commonApi.ErrorType
import services.commonApi.forError
import services.commonApi.forSuccess
import services.commonApi.requestFor
import services.metarService.model.WxMetar
import services.metarService.model.WxTaf
import common.BuildKonfig

class CheckWxMetarService(
    private val client: HttpClient
) : MetarService {
    override suspend fun getMetar(station: String): Result<String> {
        val headers = mapOf("X-API-Key" to BuildKonfig.WXAPI_KEY)
        val metar = client.requestFor<WxMetar>(HttpRoutes.CHECK_WX_METAR + station, headers)
        var metarString: String? = null
        metar.forSuccess {
            if (it.results > 0) metarString = it.data.first()
        }
        metar.forError { type, message ->
            return Result.Error(type, message)
        }
        return if (metarString != null) {
            Result.Success(metarString)
        } else {
            Result.Error(ErrorType.EMPTY_RESULT, "No METAR data for station $station")
        }
    }

    override suspend fun getTaf(station: String): Result<String> {
        val headers = mapOf("X-API-Key" to BuildKonfig.WXAPI_KEY)
        val taf = client.requestFor<WxTaf>(HttpRoutes.CHECK_WX_TAF + station, headers)
        var tafString: String? = null
        taf.forSuccess {
            if (it.results > 0) tafString = it.data.first()
        }
        return if (tafString != null) {
            Result.Success(tafString)
        } else {
            Result.Success("")
        }
    }
}