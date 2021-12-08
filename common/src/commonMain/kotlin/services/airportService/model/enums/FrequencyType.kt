package services.airportService.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FrequencyType {
    @SerialName("TML") TERMINAL,
    @SerialName("CTAF") ADVISORY_SERVICE,
    @SerialName("CTR") RADAR,
    @SerialName("UNKNOWN") UNRECOGNIZED,
    @SerialName("AFIS") FLIGHT_INFORMATION_SERVICE,
    @SerialName("APP") APPROACH,
    @SerialName("RMP") APRON,
    @SerialName("ARR") ARRIVAL,
    @SerialName("ATIS") WEATHER_OBSERVATION,
    @SerialName("UNIC") UNICOM,
    @SerialName("DEL") DELIVERY,
    @SerialName("CTR") RADAR_CONTROL,
    @SerialName("DEP") DEPARTURE,
    @SerialName("FIRE") EMERGENCY,
    @SerialName("GND") GROUND,
    @SerialName("TWR") TOWER
}