package services.airportService.model

import services.airportService.model.enums.FrequencyType
import kotlinx.serialization.Serializable

@Serializable
data class Frequency(
    val type: FrequencyType,
    val description: String? = null,
    val valueMhz: Float
)
