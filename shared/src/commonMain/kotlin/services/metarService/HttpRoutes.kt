package services.metarService

object HttpRoutes {
    const val METAR_TAF = "https://metartaf.ru"

    private const val CHECK_WXAPI_BASE = "https://api.checkwx.com/"
    const val CHECK_WX_METAR = CHECK_WXAPI_BASE + "metar/"
    const val CHECK_WX_TAF = CHECK_WXAPI_BASE + "taf/"

    private const val AVIATION_WEATHER_BASE = "https://aviationweather.gov/api/data"
    const val AVIATION_WEATHER_METAR = "$AVIATION_WEATHER_BASE/metar"
    const val AVIATION_WEATHER_TAF = "$AVIATION_WEATHER_BASE/taf"

    private const val ATIS_INFO_BASE = "https://atis.info/api"
    const val ATIS_INFO = ATIS_INFO_BASE
}