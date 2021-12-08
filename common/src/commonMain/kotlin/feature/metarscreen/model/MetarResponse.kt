package feature.metarscreen.model

import services.metarService.model.MetarTaf
import kotlin.math.roundToInt

const val METER_TO_KNOT = 1.94384f

data class Metar(
    val icao: String,
    val time: String,
    val windDirection: Int,
    val windSpeedKt: Int,
    val other: String
)

fun MetarTaf.parseMetar(): Metar? {
    return try {
        val (_, metarRaw) = metar.split("\n")
        val raw = metarRaw.split(" ")
        Metar(
            icao = raw[0],
            time = raw[1],
            windDirection = raw[2].substring(0, 3).toInt(),
            windSpeedKt = (raw[2].substring(3, 5).toFloat() * if (raw[2].uppercase().contains("MPS")) METER_TO_KNOT else 1f).roundToInt(),
            other = raw.drop(3).joinToString(" ")
        )
    } catch (E: RuntimeException) {
        null
    }
}