package model

import androidx.compose.ui.res.useResource
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

actual fun loadAircraftFromJSON(filename: String): List<Aircraft>? =
    try {
        useResource(filename) {
            val json = Json.decodeFromString<List<Aircraft>>(it.bufferedReader().readText())
            print(json)
            return json
        }
} catch (E: IllegalArgumentException) { null }



