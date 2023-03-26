package services.airportService.model.enums

enum class FrequencyType(val csv: String) {
    TERMINAL("TML"),
    ADVISORY_SERVICE("CTAF"),
    UNRECOGNIZED("UNKNOWN"),
    FLIGHT_INFORMATION_SERVICE("AFIS"),
    APPROACH("APP"),
    APRON("RMP"),
    ARRIVAL("ARR"),
    WEATHER_OBSERVATION("ATIS"),
    UNICOM("UNIC"),
    DELIVERY("DEL"),
    RADAR_CONTROL("CTR"),
    DEPARTURE("DEP"),
    EMERGENCY("FIRE"),
    GROUND("GND"),
    TOWER("TWR"),
    RADAR("RDR")
}