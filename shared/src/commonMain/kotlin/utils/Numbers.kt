package utils

import kotlin.math.*

fun Int.ifZero(value: () -> Int): Int = if (this != 0) this else value()

fun String.filterDigitsToInt(): Int = filter { it.isDigit() }.toInt()

fun angleBetweenHeadings(heading1: Int, heading2: Int): Int {
    val h1rad = (90 - heading1) * PI / 180f
    val h2rad = (90 - heading2) * PI / 180f
    val h1x = cos(h1rad)
    val h1y = sin(h1rad)
    val h2x = cos(h2rad)
    val h2y = sin(h2rad)
    return ((acos(h1x * h2x + h1y * h2y)) * 180 / PI).roundToInt()
}