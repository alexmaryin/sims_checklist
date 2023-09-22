package services.airportService.model.enums

enum class AirportType(val csv: String) {
    CLOSED("closed_airport"),
    HELIPORT("heliport"),
    LARGE("large_airport"),
    MEDIUM("medium_airport"),
    SEA_BASE("seaplane_base"),
    SMALL("small_airport")
}
