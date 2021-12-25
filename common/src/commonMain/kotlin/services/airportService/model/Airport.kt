package services.airportService.model

import services.airportService.model.enums.AirportType
import kotlinx.serialization.Serializable

@Serializable
data class Airport(
    val icao: String,
    val type: AirportType,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val elevation: Int,
    val webSite: String? = null,
    val wiki: String? = null,
    val frequencies: List<Frequency> = emptyList(),
    val runways: List<Runway> = emptyList()
)
