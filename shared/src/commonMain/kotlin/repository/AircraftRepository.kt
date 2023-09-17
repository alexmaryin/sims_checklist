package repository

import feature.checklists.model.Aircraft
import feature.checklistDetails.model.Checklist

interface AircraftRepository {
    fun getAll(): List<Aircraft>
    fun getById(id: Int): Aircraft
    fun getChecklist(aircraftId: Int, checklistId: Int): Checklist
    fun toggleChecklistItem(aircraftId: Int, checklistId: Int, itemId: Int)
    fun clearChecklist(aircraftId: Int, checklistId: Int)
    fun clearBaseChecklists(aircraftId: Int)
    fun undoChecklistChanges(aircraftId: Int, checklistId: Int)
}
