package services.airportService

import AppAndroid

actual fun getFilePath(filename: String) =
    with(AppAndroid.instance()) {
        "$filesDir/$filename"
    }