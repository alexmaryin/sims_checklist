package services.metarService

object HttpRoutes {
    const val METAR_TAF = "https://metartaf.ru"

    private const val CHECK_WXAPI_BASE = "https://api.checkwx.com/"
    const val CHECK_WX_METAR = CHECK_WXAPI_BASE + "metar/"
    const val CHECK_WX_TAF = CHECK_WXAPI_BASE + "taf/"
}