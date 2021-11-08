package repository

import database.AircraftBase
import feature.checklists.model.Aircraft
import feature.checklists.model.Checklist

class AircraftRepositoryImpl(
    database: AircraftBase
) : AircraftRepository {

    private var base: List<Aircraft>? = database.getAircraft()

    override fun getAll(): List<Aircraft> =
        base ?: throw IllegalStateException("Can't find files with aircraft!")

    override fun getById(id: Int): Aircraft =
        base?.firstOrNull { it.id == id } ?: throw IllegalArgumentException("Wrong aircraft id!")

    override fun getChecklist(aircraftId: Int, checklistId: Int): Checklist =
        base?.firstOrNull { it.id == aircraftId }?.checklists?.firstOrNull { it.id == checklistId } ?:
        throw IllegalArgumentException("Wrong checklist id!")

    override fun updateBaseChecklist(aircraftId: Int, checklistId: Int, newValues: List<Boolean>) {
        base?.let { aircraft ->
            aircraft.first { it.id == aircraftId }
                .checklists.first { it.id == checklistId }
                .items.zip(newValues) { oldItem, value ->
                oldItem.checked = value
            }
        }
    }

    override fun clearBaseChecklists(aircraftId: Int) {
        base?.let { aircraft ->
            aircraft.first { it.id == aircraftId }.checklists.forEach { checklist ->
                checklist.items.forEach { item ->
                    item.checked = false
                }
            }
        }
    }
}
