package feature.metarParser

enum class PhenomenonIntensity { NONE, HIGH, LIGHT }

enum class WeatherPhenomenons(val code: String) {
    DRIZZLE("DZ"), RAIN("RA"), SNOW("SN"), SNOW_GRAINS("SG"), ICE_PELLETS("PL"), SMALL_HAIL("GS"),
    HAIL("GR"), SHOWER("SH"), FREEZE("FZ"), THUNDERSTORM("TS"), DUST_STORM("DS"), SANDSTORM("SS"),
    FOG("FG"), IN_VICINITY("VC"), SHALLOW("MI"), PARTIAL("PR"), PATCHES("BC"), MIST("BR"),
    HAZE("HZ"), SMOKE("FU"), DUST("DU"), BLOWING("BL"), SQUALL("SQ"), ICE_CRYSTALS("IC"),
    VOLCANIC_ASH("VA"), DRIFTING("DR"), SAND("SA")
}

data class Phenomenons(
    val all: Set<WeatherPhenomenons> = emptySet(),
    val intensity: PhenomenonIntensity = PhenomenonIntensity.NONE
)