package model

interface AircraftBase {
    fun getAll(): List<Aircraft>
    fun getById(id: Int): Aircraft
    fun getChecklist(aircraftId: Int, checklistId: Int): Checklist
}
