package model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AircraftBaseTestImpl : AircraftBase {

    private val testAircraft = Aircraft(
        id = 0,
        name = "Cessna 172 Skyhawk",
        performance = Performance(
            fuelCapacity = 28f,
            averageCruiseSpeed = 100f,
            averageFuelFlow = 8f,
        ),
        checklists = listOf(
            Checklist(
                id = 0,
                caption = "Preflight",
                items = listOf(
                    Item(caption = "Walk around", details = "Look up for last crash"),
                    Item(caption = "Fuel check", details = "Filling the tanks"),
                    Item(caption = "Documents ob board", details = "Don't forget this app"),
                    Item(caption = "LINE", checked = true),
                    Item(caption = "Below the line"),
                    Item(caption = "Battery ON", details = "Check the alternator"),
                    Item(caption = "Parking brakes ON"),
                )
            )
        )
    )

    override fun getAll(): List<Aircraft> = listOf(testAircraft)

    override fun getById(id: Int): Aircraft =
        if(id == 0) testAircraft else throw NotImplementedError("Only one for test")

    override fun getChecklist(aircraftId: Int, checklistId: Int): Checklist =
        getById(aircraftId).checklists.first { it.id == checklistId }

    private fun saveToJSON() {
        val json = Json.encodeToString(testAircraft)
        println(json)
    }

    init {
        saveToJSON()
    }
}
