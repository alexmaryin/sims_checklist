package services.airportService.model.enums


enum class RunwaySurface(val csv: String) {
    ASPHALT("ASP"),
    TURF("TURF"),
    CONCRETE("CON"),
    GRASS("GRS"),
    GRAVEL("GRE"),
    WATER("WATER"),
    UNKNOWN("UNK")
}