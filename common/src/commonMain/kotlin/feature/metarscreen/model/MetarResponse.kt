package feature.metarscreen.model

import alexmaryin.metarkt.MetarParser
import alexmaryin.metarkt.models.Metar
import services.metarService.model.MetarTaf

fun MetarTaf.parseMetar(): Metar {
    val metarRaw = metar.split("\n").last()
    return MetarParser.current().parse(metarRaw)
}