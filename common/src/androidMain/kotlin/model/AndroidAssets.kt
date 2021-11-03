package model

import ru.alexmaryin.simschecklist.AppAndroid

actual fun loadAircraftJson(filename: String) = AppAndroid.instance().loadAircraft(filename)
