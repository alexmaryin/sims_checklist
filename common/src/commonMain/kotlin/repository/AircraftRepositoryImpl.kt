package repository

import database.AircraftBase
import feature.checklists.model.Aircraft
import feature.checklistDetails.model.Checklist

class AircraftRepositoryImpl(
    database: AircraftBase
) : AircraftRepository {

    private var base: List<Aircraft>? = database.getAircraft()

    private var previousChecklistState: List<Boolean>? = null

    override fun getAll(): List<Aircraft> =
        base ?: throw IllegalStateException("Can't find files with aircraft!")

    override fun getById(id: Int): Aircraft =
        base?.firstOrNull { it.id == id }
            ?: throw IllegalArgumentException("Wrong aircraft id!")

    override fun getChecklist(aircraftId: Int, checklistId: Int): Checklist =
        getById(aircraftId).checklists.firstOrNull { it.id == checklistId }
            ?: throw IllegalArgumentException("Wrong checklist id!")

    override fun toggleChecklistItem(aircraftId: Int, checklistId: Int, itemId: Int) {
        with(getChecklist(aircraftId, checklistId)) {
            items[itemId].checked = !items[itemId].checked
        }
    }

    override fun clearChecklist(aircraftId: Int, checklistId: Int) {
        previousChecklistState = getChecklist(aircraftId, checklistId).items.map { it.checked }
        with(getChecklist(aircraftId, checklistId)) {
            items.forEach { it.checked = false }
        }
    }

    override fun clearBaseChecklists(aircraftId: Int) {
        with(getById(aircraftId)) {
            checklists.forEach { checklist ->
                checklist.items.forEach { item ->
                    item.checked = false
                }
            }
        }
    }

    override fun undoChecklistChanges(aircraftId: Int, checklistId: Int) {
        previousChecklistState?.let { old ->
            with(getChecklist(aircraftId, checklistId)) {
                items.zip(old) { item, oldValue ->
                    item.checked = oldValue
                }
            }
        }
    }
}

