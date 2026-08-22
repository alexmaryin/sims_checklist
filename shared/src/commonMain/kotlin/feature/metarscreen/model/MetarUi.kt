package feature.metarscreen.model

import kotlinx.serialization.Serializable

typealias Heading = Int

const val INIT_WIND_HEADING = 360
const val INIT_WIND_SPEED = 5

@Serializable
data class MetarUi(
    val airport: String = "",
    val userAngle: Heading = INIT_WIND_HEADING,
    val userSpeed: Int = INIT_WIND_SPEED,
    val metarAngle: Heading? = null,
    val metarSpeedKt: Int? = null,
    val rawMetar: String = "",
    val rawTaf: String = "",
    val lastIcao: String? = null
)