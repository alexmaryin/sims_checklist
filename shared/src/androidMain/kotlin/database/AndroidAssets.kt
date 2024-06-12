package database

import ru.alexmaryin.simschecklist.AppAndroid
import ru.alexmaryin.simschecklist.R

actual fun loadAircraftJson(filename: String) =
    with(AppAndroid.instance()) {
        resources.openRawResource(R.raw.aircraft).bufferedReader().readText()
    }
