package database

import feature.checklists.model.Aircraft

interface AircraftBase {
    fun getAircraft(): List<Aircraft>?
}