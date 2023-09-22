package database

import ru.alexmaryin.simschecklist.AppAndroid

actual fun loadAircraftJson(filename: String) =
    with(AppAndroid.instance()) {
        val resourceId = resources.getIdentifier(
            filename.substringBefore("."), "raw", packageName
        )
        resources.openRawResource(resourceId)
            .bufferedReader().readText()
    }
