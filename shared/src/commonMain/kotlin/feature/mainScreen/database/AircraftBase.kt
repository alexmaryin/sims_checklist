package feature.mainScreen.database

import feature.checklists.model.Aircraft

interface AircraftBase {
    suspend fun getAircraft(): List<Aircraft>?

    /** Warnings from the last [getAircraft] call: aircraft skipped because their files are missing or invalid. */
    val loadWarnings: List<String>
}
