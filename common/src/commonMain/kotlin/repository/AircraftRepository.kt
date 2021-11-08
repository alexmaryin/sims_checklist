package repository

import feature.checklists.model.Aircraft
import feature.checklists.model.Checklist

interface AircraftRepository {
    fun getAll(): List<Aircraft>
    fun getById(id: Int): Aircraft
    fun getChecklist(aircraftId: Int, checklistId: Int): Checklist
    fun updateBaseChecklist(aircraftId: Int, checklistId: Int, newValues: List<Boolean>)
    fun clearBaseChecklists(aircraftId: Int)
}
