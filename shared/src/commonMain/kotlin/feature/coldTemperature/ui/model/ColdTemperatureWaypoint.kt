package feature.coldTemperature.ui.model

import kotlinx.serialization.Serializable
import services.coldTemperature.ApproachSegment

@Serializable
data class ColdTemperatureWaypoint(
    val name: String,
    val altitudeFeet: Int,
    val segment: ApproachSegment = ApproachSegment.INTERMEDIATE,
    val correctedAltitudeFeet: Int = 0
)