package repository

import feature.mainScreen.database.AircraftBase
import feature.checklistDetails.model.CHECKLIST_LINE
import feature.checklistDetails.model.Checklist
import feature.checklists.model.Aircraft
import kotlinx.coroutines.runBlocking

class AircraftRepositoryImpl(
    database: AircraftBase
) : AircraftRepository {

    private var base: List<Aircraft>? = runBlocking { database.getAircraft() }

    private var previousChecklistState: List<Boolean>? = null

    override fun getAll(): List<Aircraft> =
        base ?: throw IllegalStateException("Can't find files with aircraft!")

    override fun getById(id: Int): Aircraft =
        base?.firstOrNull { it.id == id }
            ?: throw IllegalArgumentException("Wrong aircraft id!")

    override fun getChecklist(aircraftId: Int, checklistId: Int): Checklist =
        getById(aircraftId).checklists.firstOrNull { it.id == checklistId }
            ?: throw IllegalArgumentException("Wrong checklist id!")

    private fun updateChecklist(aircraftId: Int, checklistId: Int, transform: (Checklist) -> Checklist) {
        val aircraft = getById(aircraftId)
        val newChecklists = aircraft.checklists.map { if (it.id == checklistId) transform(it) else it }
        val newAircraft = aircraft.copy(checklists = newChecklists)
        base = base?.map { if (it.id == aircraftId) newAircraft else it }
    }

    override fun toggleChecklistItem(aircraftId: Int, checklistId: Int, itemId: Int) {
        updateChecklist(aircraftId, checklistId) { checklist ->
            val newItems = checklist.items.mapIndexed { index, item ->
                if (index == itemId) item.copy(checked = !item.checked) else item
            }
            checklist.copy(items = newItems, isCompleted = newItems
                .filterNot { it.caption == CHECKLIST_LINE }
                .all { it.checked })
        }
    }

    override fun clearChecklist(aircraftId: Int, checklistId: Int) {
        updateChecklist(aircraftId, checklistId) { checklist ->
            previousChecklistState = checklist.items.map { it.checked }
            val newItems = checklist.items.map { it.copy(checked = false) }
            checklist.copy(items = newItems, isCompleted = false)
        }
    }

    override fun clearBaseChecklists(aircraftId: Int) {
        val aircraft = getById(aircraftId)
        val newChecklists = aircraft.checklists.map { checklist ->
            val newItems = checklist.items.map { item -> item.copy(checked = false) }
            checklist.copy(items = newItems, isCompleted = false)
        }
        val newAircraft = aircraft.copy(checklists = newChecklists)
        base = base?.map { if (it.id == aircraftId) newAircraft else it }
    }

    override fun undoChecklistChanges(aircraftId: Int, checklistId: Int) {
        previousChecklistState?.let { old ->
            updateChecklist(aircraftId, checklistId) { checklist ->
                val newItems = checklist.items.mapIndexed { index, item -> item.copy(checked = old[index]) }
                checklist.copy(items = newItems, isCompleted = newItems
                    .filterNot { it.caption == CHECKLIST_LINE }
                    .all { it.checked })
            }
        }
    }
}
