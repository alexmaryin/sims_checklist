package model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

expect fun loadAircraftJson(filename: String): String

const val AIRCRAFT_FILE = "aircraft.json"

class AircraftBaseImpl : AircraftBase {

    private val base: List<Aircraft>? = try { Json.decodeFromString(loadAircraftJson(AIRCRAFT_FILE)) } catch (E: IllegalArgumentException) { null }

    override fun getAll(): List<Aircraft> =
        base ?: throw RuntimeException("Can't find files with aircraft!")

    override fun getById(id: Int): Aircraft =
        base?.firstOrNull { it.id == id } ?: throw RuntimeException("Wrong aircraft id!")

    override fun getChecklist(aircraftId: Int, checklistId: Int): Checklist =
        base?.firstOrNull { it.id == aircraftId }?.checklists?.firstOrNull { it.id == checklistId } ?: throw RuntimeException("Wrong checklist id!")
}