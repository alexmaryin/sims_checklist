package database

import feature.checklists.model.Aircraft
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

const val AIRCRAFT_FILE = "aircraft.json"

expect fun loadAircraftJson(filename: String): String

class AircraftBaseImpl : AircraftBase {
    override fun getAircraft(): List<Aircraft>? =
        try {
            Json.decodeFromString(loadAircraftJson(AIRCRAFT_FILE))
        }
        catch (E: IllegalArgumentException) {
            null
        }
}