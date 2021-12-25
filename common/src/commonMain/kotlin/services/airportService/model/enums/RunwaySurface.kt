package services.airportService.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RunwaySurface {
    @SerialName("ASP") ASPHALT,
    @SerialName("TURF") TURF,
    @SerialName("CON") CONCRETE,
    @SerialName("GRS") GRASS,
    @SerialName("GRE") GRAVEL,
    @SerialName("WATER") WATER,
    @SerialName("UNK") UNKNOWN
}