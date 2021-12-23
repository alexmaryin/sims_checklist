package feature.metarParser

object MetarGroups {
    val STATION = "^[A-Z]{4}".toRegex()
    val REPORT_TIME = "([0-9]{2})([0-9]{2})([0-9]{2})Z".toRegex()
    val WIND = "([0-9]{3}|VRB)([0-9]{2,3})G?([0-9]{2,3})?(KT|MPS|KMH)".toRegex()
    val VISIBILITY = "^([0-9]{4})([NS]?[EW]?)".toRegex()
    val CLOUDS = "^(VV|FEW|SCT|SKC|CLR||BKN|OVC)([0-9]{3}|///)(CU|CB|TCU|CI)?".toRegex()
    val TEMPERATURE_DEW = "^(M?[0-9]{2})/(M?[0-9]{2})?".toRegex()
    val PRESSURE_HG = "A([0-9]{4})".toRegex()
    val PRESSURE_MB = "Q([0-9]{4})".toRegex()
    val PRECIPITATION = "^(VC)?(-|\\\\+)?(MI|PR|BC|DR|BL|SH|TS|FZ)?((DZ|RA|SN|SG|IC|PL|GR|GS|UP)+)?(BR|FG|FU|VA|DU|SA|HZ|PY)?(PO|SQ|FC|SS)?".toRegex()
}