package services.airportService.localService

enum class Files(val filename: String) {
    FREQUENCIES("../files/airport-frequencies.csv"),
    RUNWAYS("../files/runways.csv"),
    AIRPORTS("../files/airports.csv")
}