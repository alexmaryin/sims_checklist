package feature.coldTemperature.ui.model

import kotlinx.serialization.Serializable
import services.coldTemperature.WaypointSegment

@Serializable
data class ColdTemperatureWaypoint(
    val name: String,
    val altitudeFeet: Int,
    val segment: WaypointSegment = WaypointSegment.ABOVE_FAF,
    val correctedAltitudeFeet: Int = 0
)