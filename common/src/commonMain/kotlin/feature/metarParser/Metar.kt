package feature.metarParser

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.round

expect fun Double.formatToFloat(decimals: Int): Float

data class Metar(
    val station: String?,
    val reportTime: LocalDateTime?,
    val wind: Wind?,
    val visibility: Visibility?,
    val phenomenons: Phenomenons?,
    val clouds: List<CloudLayer>,
    val temperature: Temperature?,
    val pressureQNH: PressureQNH?
) {
    val ceilingAndVisibilityOK get() = clouds.isEmpty() && visibility?.distAll == 9999 && phenomenons != null
}

data class Wind(
    val direction: Int = 0,
    val variable: Boolean = false,
    val speed: Int = 0,
    val speedUnits: WindUnit = WindUnit.KT,
    val gusts: Int = 0
) {
    val isCalm get() = speed == 0 && direction == 0
}

data class Visibility(
    val distAll: Int? = null,
    val byDirections: List<VisibilityByDir> = emptyList(),
    val byRunways: List<VisibilityByRunway> = emptyList(),
    val distUnits: VisibilityUnit = VisibilityUnit.METERS,
    )

data class VisibilityByDir(
    val dist: Int,
    val direction: VisibilityDirection
)

data class VisibilityByRunway(
    val dist: Int,
    val runway: String
)

data class Phenomenons(
    val all: Set<WeatherPhenomenons> = emptySet(),
    val intensity: PhenomenonIntensity = PhenomenonIntensity.NONE
)

data class CloudLayer(
    val type: CloudsType,
    val lowMarginFt: Int,
    val cumulusType: CumulusType? = null
)

data class Temperature(
    val air: Int,
    val dewPoint: Int
)

data class PressureQNH(
    val hPa: Int,
    val inHg: Float
)

const val ONE_INCH_HG = 33.863886666667

enum class WindUnit { KT, MPS, KPH }

enum class VisibilityUnit { METERS, SM }

enum class VisibilityDirection { NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST }

enum class PhenomenonIntensity { NONE, HIGH, LIGHT }

enum class WeatherPhenomenons(val code: String) {
    DRIZZLE("DZ"), RAIN("RA"), SNOW("SN"), SNOW_GRAINS("SG"), ICE_PELLETS("PL"), SMALL_HAIL("GS"),
    HAIL("GR"), SHOWER("SH"), FREEZE("FZ"), THUNDERSTORM("TS"), DUST_STORM("DS"), SANDSTORM("SS"),
    FOG("FG"), IN_VICINITY("VC"), SHALLOW("MI"), PARTIAL("PR"), PATCHES("BC"), MIST("BR"),
    HAZE("HZ"), SMOKE("FU"), DUST("DU"), BLOWING("BL"), SQUALL("SQ"), ICE_CRYSTALS("IC"),
    VOLCANIC_ASH("VA"), DRIFTING("DR"), SAND("SA")
}

enum class CloudsType(val code: String) {
    CLEAR("SKC"), NIL_SIGNIFICANT("NSC"), FEW("FEW"), SCATTERED("SCT"), BROKEN("BKN"), OVERCAST("OVC")
}

enum class CumulusType { CUMULONIMBUS, TOWERING_CUMULUS }


class MetarParser(raw: String) {
    private val parts = raw.split(" ", "\t")

    private fun parseStation(): String? {
        parts.forEach { part ->
            MetarGroups.STATION.find(part)?.let {
                return it.value
            }
        }
        return null
    }

    private fun parseReportTime(): LocalDateTime? {
        parts.forEach { part ->
            MetarGroups.REPORT_TIME.find(part)?.let {
                val stamp = Clock.System.now().toLocalDateTime(TimeZone.UTC)
                return LocalDateTime(
                    stamp.year, stamp.month, it.groupValues[1].toInt(), it.groupValues[2].toInt(), it.groupValues[3].toInt()
                )
            }
        }
        return null
    }

    private fun parseWind(): Wind? {
        parts.forEach { part ->
            MetarGroups.WIND.find(part)?.let {
                return Wind(
                    direction = it.groupValues[1].toIntOrNull() ?: 0,
                    variable = it.groupValues[1] == "VRB",
                    speed = it.groupValues[2].toIntOrNull() ?: 0,
                    speedUnits = when (it.groupValues[4]) {
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

    private fun parseVisibility(): Visibility? {

        val byDirs: MutableList<VisibilityByDir> = mutableListOf()
        val byRunways: MutableList<VisibilityByRunway> = mutableListOf()

        parts.forEach { part ->
            MetarGroups.VISIBILITY.find(part)?.let {
                when {
                    it.groupValues[2].isBlank() && it.groupValues[6].isBlank() -> {
                        return Visibility(
                            distAll = it.groupValues[1].toIntOrNull() ?: it.groupValues[3].toIntOrNull()
                            ?: if (it.groupValues[5] == "CAVOK") 9999 else throw RuntimeException("Visibility undefined in part ${it.groupValues[0]}!"),
                            distUnits = if (it.groupValues[4] == "SM") VisibilityUnit.SM else VisibilityUnit.METERS,
                        )
                    }
                    it.groupValues[2].isNotBlank() -> {
                        byDirs += VisibilityByDir(
                            dist = it.groupValues[1].toIntOrNull() ?: throw RuntimeException("Visibility for direction ${it.groupValues[2]} undefined!"),
                            direction = when (it.groupValues[2]) {
                                "N" -> VisibilityDirection.NORTH
                                "NE" -> VisibilityDirection.NORTH_EAST
                                "E" -> VisibilityDirection.EAST
                                "SE" -> VisibilityDirection.SOUTH_EAST
                                "S" -> VisibilityDirection.SOUTH
                                "SW" -> VisibilityDirection.SOUTH_WEST
                                "W" -> VisibilityDirection.WEST
                                "NW" -> VisibilityDirection.NORTH_WEST
                                else -> throw RuntimeException("Unknown visibility direction ${it.groupValues[2]}!")
                            }
                        )
                    }
                    it.groupValues[6].isNotBlank() -> {
                        byRunways += VisibilityByRunway(
                            dist = it.groupValues[7].toIntOrNull() ?: throw RuntimeException("Visibility for runway ${it.groupValues[6]} undefined!"),
                            runway = it.groupValues[6]
                        )
                    }
                }
            }
        }
        return if (byDirs.isNotEmpty() || byRunways.isNotEmpty()) Visibility(byDirections = byDirs, byRunways = byRunways) else null
    }

    private fun parsePhenomenons(): Phenomenons? {

        val items = mutableSetOf<WeatherPhenomenons>()
        var intensity = PhenomenonIntensity.NONE

        parts.forEach { part ->
            MetarGroups.PHENOMENONS.find(part)?.let { match ->
                if (match.groupValues[1].isNotBlank()) {
                    intensity = when(match.groupValues[1]) {
                        "+" -> PhenomenonIntensity.HIGH
                        "-" -> PhenomenonIntensity.LIGHT
                        else -> PhenomenonIntensity.NONE
                    }
                }
                if(match.groupValues[2].isNotBlank()) {
                    match.groupValues[2].windowed(2, 2) { code ->
                        items += WeatherPhenomenons.values().first { it.code == code }
                    }
                }
            }
        }
        return if(items.isNotEmpty()) Phenomenons(items, intensity) else null
    }

    private fun parseClouds(): List<CloudLayer> {

        val items = mutableListOf<CloudLayer>()

        parts.forEach { part ->
            MetarGroups.CLOUDS.find(part)?.let { match ->
                val type = CloudsType.values().first { it.code == match.groupValues[1] }
                val cumulus = when(match.groupValues[3]) {
                    "CB" -> CumulusType.CUMULONIMBUS
                    "TCU" -> CumulusType.TOWERING_CUMULUS
                    else -> null
                }
                items += CloudLayer(
                    type = type,
                    lowMarginFt = match.groupValues[2].toIntOrNull() ?: throw RuntimeException("No margin for cloud layer in ${match.groupValues[0]}"),
                    cumulusType =  cumulus
                )
            }
        }
        return items.sortedBy { it.lowMarginFt }
    }

    private fun parseTemperature(): Temperature? {
        parts.forEach { part ->
            MetarGroups.TEMPERATURE_DEW.find(part)?.let { match ->
                return Temperature(
                    air = match.groupValues[1].replace("M", "-").toInt(),
                    dewPoint = match.groupValues[2].replace("M", "-").toInt()
                )
            }
        }
        return null
    }

    private fun parsePressure(): PressureQNH? {
        parts.forEach { part ->
            MetarGroups.PRESSURE.find(part)?.let { match ->
                if(match.groupValues[1].isNotBlank()) {
                    return PressureQNH(
                        inHg = match.groupValues[1].substringAfter('A').toInt() / 100f,
                        hPa = round(match.groupValues[1].substringAfter('A').toInt() * ONE_INCH_HG / 100).toInt()
                    )
                }
                if(match.groupValues[2].isNotBlank()) {
                    return PressureQNH(
                        hPa = match.groupValues[2].substringAfter('Q').toInt(),
                        inHg =  (match.groupValues[2].substringAfter('Q').toInt() / ONE_INCH_HG).formatToFloat(2)
                    )
                }
            }
        }
        return null
    }

    fun parse(): Metar {
        return Metar(
            station = parseStation(),
            reportTime = parseReportTime(),
            wind = parseWind(),
            visibility = parseVisibility(),
            phenomenons = parsePhenomenons(),
            clouds = parseClouds(),
            temperature = parseTemperature(),
            pressureQNH = parsePressure()
        )
    }
}
