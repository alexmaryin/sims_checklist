package services.airportService.model

import services.airportService.model.enums.AirportType
import kotlinx.serialization.Serializable

@Serializable
data class Airport(
    val icao: String,
    val type: AirportType = AirportType.MEDIUM,
    val name: String,
    val latitude: Float = 0f,
    val longitude: Float = 0f,
    val elevation: Int = 0,
    val webSite: String? = null,
    val wiki: String? = null,
    val frequencies: List<Frequency> = emptyList(),
    val runways: List<Runway> = emptyList()
)
