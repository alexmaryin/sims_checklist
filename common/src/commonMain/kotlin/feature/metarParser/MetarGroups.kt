package feature.metarParser

object MetarGroups {
    val STATION = "^[A-Z]{4}".toRegex()
    val REPORT_TIME = "([0-9]{2})([0-9]{2})([0-9]{2})Z".toRegex()
    val WIND = "([0-9]{3}|VRB)([0-9]{2,3})G?([0-9]{2,3})?(KT|MPS|KMH)".toRegex()
    val VISIBILITY = "^([0-9]{4})(N|NE|E|SE|S|SW|W|NW)?$|^([0-9]{1,2})(SM)|^(CAVOK)|^R([0-9]{2}[A-Z]?)/([0-9]{4})$".toRegex()
    val PHENOMENONS = "^(-|\\+)?((MI|PR|BC|DR|DS|BL|SH|TS|FZ|DZ|RA|SN|SG|IC|PL|GR|GS|UP|BR|FG|FU|VA|VC|DU|SA|HZ|SQ|SS)+)$".toRegex()
    val CLOUDS = "^(NSC|FEW|SCT|SKC|CLR|BKN|OVC)([0-9]{3})(CB|TCU)?$".toRegex()
    val TEMPERATURE_DEW = "^(M?[0-9]{2})/(M?[0-9]{2})?\$".toRegex()
    val PRESSURE = "^A([0-9]{4})|^Q([0-9]{4})".toRegex()
}