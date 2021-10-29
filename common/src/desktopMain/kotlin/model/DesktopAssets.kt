package model

actual fun loadAircraftJSON(filename: String): String =
    ClassLoader.getSystemResource(filename).readText()


