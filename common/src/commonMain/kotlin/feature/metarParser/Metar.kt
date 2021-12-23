package feature.metarParser

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Metar(
    val station: String?,
    val reportTime: LocalDateTime?,
    val wind: Wind?,
)

data class Wind(
    val direction: Int = 0,
    val variable: Boolean = false,
    val speed: Int = 0,
    val speedUnits: WindUnit = WindUnit.KT,
    val gusts: Int = 0
)

enum class WindUnit { KT, MPS, KPH }

class MetarParser(raw: String) {
    private val parts = raw.split(" ", "\t").toMutableList()

    private fun parseStation(): String? {
        parts.forEachIndexed { index, part ->
            MetarGroups.STATION.find(part)?.let {
                parts.removeAt(index)
                return it.value
            }
        }
        return null
    }

    private fun parseReportTime(): LocalDateTime? {
        parts.forEachIndexed { index, part ->
            MetarGroups.REPORT_TIME.find(part)?.let {
                parts.removeAt(index)
                val stamp = Clock.System.now().toLocalDateTime(TimeZone.UTC)
                return LocalDateTime(
                    stamp.year, stamp.month, it.groupValues[1].toInt(), it.groupValues[2].toInt(), it.groupValues[3].toInt()
                )
            }
        }
        return null
    }

    private fun parseWind(): Wind? {
        parts.forEachIndexed { index, part ->
            MetarGroups.WIND.find(part)?.let {
                parts.removeAt(index)
                return Wind(
                    direction = it.groupValues[1].toIntOrNull() ?: 0,
                    variable = it.groupValues[1] == "VRB",
                    speed = it.groupValues[2].toIntOrNull() ?: 0,
                    speedUnits = when(it.groupValues[4]) {
                        "MPS" -> WindUnit.MPS
                        "KPH" -> WindUnit.KPH
                        else -> WindUnit.KT
                    },
                    gusts = it.groupValues[3].toIntOrNull() ?: 0
                )
            }
        }
        return null
    }

    fun parse(): Metar {

        return Metar(
            station = parseStation(),
            reportTime = parseReportTime(),
            wind = parseWind(),
        )
    }
}

