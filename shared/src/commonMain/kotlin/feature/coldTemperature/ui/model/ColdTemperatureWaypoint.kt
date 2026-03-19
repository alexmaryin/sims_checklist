package feature.coldTemperature.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class ColdTemperatureWaypoint(
    val name: String,
    val altitudeFeet: Int,
    val correctedAltitudeFeet: Int = 0
)