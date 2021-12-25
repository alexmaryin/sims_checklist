package feature.metarscreen.model

import feature.metarParser.Metar
import feature.metarParser.MetarParser
import services.metarService.model.MetarTaf

fun MetarTaf.parseMetar(): Metar {
    val (_, metarRaw) = metar.split("\n")
    return MetarParser(metarRaw).parse()
}