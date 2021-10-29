package model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

const val TEST_DATA = """
    {
  "id": 0,
  "name": "Cessna 172 Skyhawk",
  "performance": {
    "fuelCapacity": 28,
    "averageCruiseSpeed": 100,
    "averageFuelFlow": 8
  },
  "checklists": [
    {
      "id": 0,
      "caption": "Engine start (normal)",
      "items": [
        {
          "caption": " Seatbelts/Shoulder Harness",
          "action": "FASTENED"
        },
        {
          "caption": "Brakes",
          "action": "TEST & SET"
        },
        {
          "caption": "Fuel selector",
          "action": "BOTH"
        },
        {
          "caption": "Fuel Shutoff Valve",
          "action": "ON (IN)"
        },
        {
          "caption": "Circuit Breakers",
          "action": "CHECK"
        },
        {
          "caption": "Beacon",
          "action": "ON"
        },
        {
          "caption": "Avionics Switch",
          "action": "OFF"
        },
        {
          "caption": "Master Switch",
          "action": "ON"
        },
        {
          "caption": "Throttle",
          "action": "OPEN",
          "details": "Open on 1/4 inch"
        },
        {
          "caption": "Mixture",
          "action": "IDLE CUTOFF",
          "details": "If engine cold:\na. Aux. pump...ON\nb. Mixture...Rich until 3-5 GPH then CUT OFF\nc. Aux. pump...OFF"
        },
        {
          "caption": "Propeller Area",
          "action": "CLEAR"
        },
        {
          "caption": "Starter",
          "action": "ENGAGE"
        }
      ]
    }
  ]
}
"""

class AircraftBaseTestImpl : AircraftBase {

    private val testAircraft = Json.decodeFromString<Aircraft>(TEST_DATA)

    override fun getAll(): List<Aircraft> = listOf(testAircraft)

    override fun getById(id: Int): Aircraft =
        if(id == 0) testAircraft else throw NotImplementedError("Only one for test")

    override fun getChecklist(aircraftId: Int, checklistId: Int): Checklist =
        getById(aircraftId).checklists.first { it.id == checklistId }
}
