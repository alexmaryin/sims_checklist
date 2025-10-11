package feature.mainScreen.database

import feature.checklists.model.Aircraft
import kotlinx.serialization.json.Json
import sims_checklist.shared.generated.resources.Res

const val AIRCRAFT_FILE = "aircraft.json"

class AircraftBaseImpl : AircraftBase {
    override suspend fun getAircraft(): List<Aircraft>? =
        try {
            val json = Res.readBytes("files/$AIRCRAFT_FILE").decodeToString()
            Json.decodeFromString(json)
        }
        catch (_: IllegalArgumentException) {
            null
        }
}