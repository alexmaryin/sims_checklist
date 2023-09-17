package commonTest

import database.AircraftBase
import feature.checklists.model.Aircraft
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import repository.AircraftRepository
import repository.AircraftRepositoryImpl
import kotlin.test.*

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

internal class AircraftBaseTestImplTest {

    private lateinit var aircraftBaseTestImpl: AircraftBase
    private lateinit var repository: AircraftRepository

    @BeforeTest
    fun startup() {
        aircraftBaseTestImpl = object  : AircraftBase {
            override fun getAircraft(): List<Aircraft>? =
                try { listOf(Json.decodeFromString(TEST_DATA)) } catch (E: IllegalArgumentException) { null }

        }
        repository = AircraftRepositoryImpl(aircraftBaseTestImpl)
    }

    @Test
    fun `get all should return one test aircraft`() {
        assertTrue { repository.getAll() == listOf(Json.decodeFromString<Aircraft>(TEST_DATA)) }
    }

    @Test
    fun `get by id != 0 should raise exception`() {
        assertFailsWith<IllegalArgumentException> { repository.getById(1) }
    }

    @Test
    fun `get checklist should return first of its`() {
        assertTrue { repository.getChecklist(0, 0).caption == "Engine start (normal)" }
    }

    @Test
    fun `update all items of checklist to true should change isCompleted`() {
        repository.getChecklist(0, 0).items.forEach {
            it.checked = true
        }
        assertTrue { repository.getChecklist(0, 0).isCompleted }
    }

    @Test
    fun `clearing checklist should update isCompleted`() {
        repository.getChecklist(0, 0).items.forEach {
            it.checked = listOf(false, true).random()
        }
        repository.clearBaseChecklists(0)
        assertTrue { repository.getChecklist(0, 0).isCompleted.not() }
    }

    @Test
    fun `undo should return previous state of aircraft's checklists`() {
        val testValues = List(12) { listOf(true, false).random() }
        testValues.forEachIndexed { index, value ->
            if (value) repository.toggleChecklistItem(0, 0, index)
        }
        repository.clearChecklist(0, 0)
        repository.undoChecklistChanges(0, 0)
        assertTrue {
            repository.getChecklist(0, 0).items.map { it.checked } == testValues
        }
    }
}
