package utils

import kotlin.math.abs
import kotlin.math.roundToInt

fun Int.ifZero(value: () -> Int): Int = if (this != 0) this else value()

fun String.filterDigitsToInt(): Int? = filter { it.isDigit() }.toIntOrNull()


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