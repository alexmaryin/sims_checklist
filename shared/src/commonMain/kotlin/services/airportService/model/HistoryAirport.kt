package services.airportService.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryAirport(
    val timestamp: Long,
    val icao: String,
    val name: String
)
