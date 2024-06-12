package services.airportService.model

data class HistoryAirport(
    val timestamp: Long,
    val icao: String,
    val name: String
)
