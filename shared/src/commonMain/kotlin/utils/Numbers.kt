package utils

fun Int.ifZero(value: () -> Int): Int = if (this != 0) this else value()

fun String.filterDigitsToInt(): Int = filter { it.isDigit() }.toInt()