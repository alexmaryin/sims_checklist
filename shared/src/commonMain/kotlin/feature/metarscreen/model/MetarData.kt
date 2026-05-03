package feature.metarscreen.model

import alexmaryin.metarkt.models.Metar
import alexmaryin.metarkt.models.Wind
import kotlinx.serialization.Serializable

@Serializable
data class MetarData(
    val pressureQFEmmHg: Int,
    val temperature: Int,
    val windDirection: Int,
    val windSpeedKt: Int
)

fun MetarData.toWind() : Wind = Wind(direction = windDirection, speed = windSpeedKt)

fun Metar.toMetarData() = MetarData(
    pressureQFEmmHg = pressureQFE?.mmHg ?: 0,
    temperature = temperature?.air ?: 0,
    windDirection = wind?.direction ?: 0,
    windSpeedKt = wind?.speedKt ?: 0
)
