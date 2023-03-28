package database

import ru.alexmaryin.simschecklist.AppAndroid
import ru.alexmaryin.simschecklist.common.R

actual fun loadAircraftJson(filename: String) =
    with(AppAndroid.instance()) {
        resources.openRawResource(R.raw.aircraft)
            .bufferedReader().readText()
    }
