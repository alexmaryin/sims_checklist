package feature.metarscreen.model

import alexmaryin.metarkt.MetarParser
import alexmaryin.metarkt.models.Metar
import services.metarService.model.MetarTaf

fun MetarTaf.parseMetar(): Metar {
    val (_, metarRaw) = metar.split("\n")
    return MetarParser.current().parse(metarRaw)
}