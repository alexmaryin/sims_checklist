package feature.metarscreen.model

import alexmaryin.metarkt.MetarParser
import alexmaryin.metarkt.models.Metar

fun parseMetar(metarRaw: String): Metar {
    val raw = metarRaw.split("\n").last()
    return MetarParser.current().parse(raw)
}