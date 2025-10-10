package feature.mainScreen.database

import feature.checklists.model.Aircraft

interface AircraftBase {
    suspend fun getAircraft(): List<Aircraft>?
}