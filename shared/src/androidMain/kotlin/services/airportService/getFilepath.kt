package services.airportService

import ru.alexmaryin.simschecklist.AppAndroid


actual fun getFilePath(filename: String) =
    with(AppAndroid.instance()) {
        "$filesDir/$filename"
    }