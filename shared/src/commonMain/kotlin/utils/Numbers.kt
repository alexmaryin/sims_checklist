package utils

import feature.metarscreen.model.Heading
import kotlin.math.*

fun Int.ifZero(value: () -> Int): Int = if (this != 0) this else value()

fun String.filterDigitsToInt(): Int? = filter { it.isDigit() }.toIntOrNull()

fun angleBetweenHeadings(heading1: Int, heading2: Int): Int {
    val h1rad = (90 - heading1).toRadians()
    val h2rad = (90 - heading2).toRadians()
    val h1x = cos(h1rad)
    val h1y = sin(h1rad)
    val h2x = cos(h2rad)
    val h2y = sin(h2rad)
    return (acos(h1x * h2x + h1y * h2y)).radToDegrees().roundToInt()
}

fun Heading.toRadians() = this * PI / 180

fun Double.radToDegrees() = this * 180 / PI

/**
 * Converts a decimal degree value into a Degrees, Minutes, Seconds (DMS) string.
 *
 * @param isLatitude `true` if the coordinate is latitude, `false` for longitude.
 * This determines the cardinal direction (N/S for latitude, E/W for longitude).
 * @return A formatted DMS string, e.g., "55°35'57" N".
 */
fun Float.toDMS(isLatitude: Boolean): String {
    val absolute = abs(this)
    val degrees = absolute.toInt()
    val minutesNotTruncated = (absolute - degrees) * 60
    val minutes = minutesNotTruncated.toInt() // Truncate to get integer minutes
    val seconds = (minutesNotTruncated - minutes) * 60 // The remainder is seconds

    val direction = if (isLatitude) {
        if (this >= 0) "N" else "S"
    } else {
        if (this >= 0) "E" else "W"
    }

    return "%02d°%02d'%02d\" %s".format(degrees, minutes, seconds.roundToInt(), direction)
}