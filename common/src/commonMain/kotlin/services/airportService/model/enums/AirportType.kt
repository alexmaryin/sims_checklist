package services.airportService.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AirportType {
    @SerialName("closed_airport") CLOSED,
    @SerialName("heliport") HELIPORT,
    @SerialName("large_airport") LARGE,
    @SerialName("medium_airport") MEDIUM,
    @SerialName("seaplane_base") SEA_BASE,
    @SerialName("small_airport") SMALL
}
