package services.airportService.model

import services.airportService.model.enums.RunwaySurface
import kotlinx.serialization.Serializable

@Serializable
data class Runway(
    val lengthFeet: Int? = null,
    val widthFeet: Int? = null,
    val surface: RunwaySurface,
    val closed: Boolean,
    val lowNumber: String,
    val lowElevationFeet: Int? = null,
    val lowHeading: Int,
    val highNumber: String,
    val highElevationFeet: Int? = null,
    val highHeading: Int,
)

