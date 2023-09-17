package services.airportService.localService

enum class Files(val filename: String) {
    FREQUENCIES("airport-frequencies.csv"),
    RUNWAYS("runways.csv"),
    AIRPORTS("airports.csv")
}