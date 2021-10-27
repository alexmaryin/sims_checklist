package model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

const val TEST_DATA = """
    {"id":0,"name":"Cessna 172 Skyhawk","performance":{"fuelCapacity":28.0,"averageCruiseSpeed":100.0,"averageFuelFlow":8.0},
    "checklists":[{"id":0,"caption":"Preflight","items":[{"caption":"Walk around","details":"Look up for last crash"},
    {"caption":"Fuel check","details":"Filling the tanks"},{"caption":"Documents ob board","details":"Don't forget this app"},
    {"caption":"LINE","checked":true},{"caption":"Below the line"},{"caption":"Battery ON","details":"Check the alternator"},
    {"caption":"Parking brakes ON"}]}]}
"""

class AircraftBaseTestImpl : AircraftBase {

    private val testAircraft = Json.decodeFromString<Aircraft>(TEST_DATA)

    override fun getAll(): List<Aircraft> = listOf(testAircraft)

    override fun getById(id: Int): Aircraft =
        if(id == 0) testAircraft else throw NotImplementedError("Only one for test")

    override fun getChecklist(aircraftId: Int, checklistId: Int): Checklist =
        getById(aircraftId).checklists.first { it.id == checklistId }
}
