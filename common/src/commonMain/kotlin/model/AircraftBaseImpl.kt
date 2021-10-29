package model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

expect fun loadAircraftJSON(filename: String): String

val DATA = """
    [{"id":0,"name":"Cessna 172 Skyhawk","performance":{"fuelCapacity":28,"averageCruiseSpeed":100,"averageFuelFlow":8},"checklists":[{"id":0,"caption":"Engine start (normal)","items":[{"caption":"Seatbelts/Shoulder Harness","action":"FASTENED"},{"caption":"Brakes","action":"TEST & SET"},{"caption":"Fuel selector","action":"BOTH"},{"caption":"Fuel Shutoff Valve","action":"ON (IN)"},{"caption":"Circuit Breakers","action":"CHECK"},{"caption":"Beacon","action":"ON"},{"caption":"Avionics Switch","action":"OFF"},{"caption":"Master Switch","action":"ON"},{"caption":"Throttle","action":"OPEN","details":"Open on 1/4 inch"},{"caption":"Mixture","action":"IDLE CUTOFF","details":"If engine cold:\na. Aux. pump...ON\nb. Mixture...Rich until 3-5 GPH then CUT OFF\nc. Aux. pump...OFF"},{"caption":"Propeller Area","action":"CLEAR"},{"caption":"Starter","action":"ENGAGE"}]},{"id":1,"caption":"After engine start","items":[{"caption":"Ignition Switch","action":"BOTH"},{"caption":"Mixture (At Engine Start)","action":"RICH"},{"caption":"Engine","action":"1000 RPM"},{"caption":"Oil Pressure","action":"CHECK"},{"caption":"Mixture","action":"LEANED MAX"},{"caption":"Flaps","action":"RETRACT"},{"caption":"Avionics","action":"ON"},{"caption":"Instruments","action":"SET"}]},{"id":2,"caption":"Taxi","items":[{"caption":"Brakes","action":"CHECK"},{"caption":"Magnetic Compass","action":"MOVEMENT FREE"},{"caption":"Flight Instruments","action":"CHECK"}]},{"id":3,"caption":"Before takeoff","items":[{"caption":"Parking Brakes","action":"SET"},{"caption":"Flight Controls","action":"FREE & CORRECT"},{"caption":"Flight Instruments","action":"SET"},{"caption":"Fuel Selector","action":"BOTH"},{"caption":"Elevator Trim","action":"SET"},{"caption":"Mixture","action":"RICH FOR RUN-UP"},{"caption":"Autopilot","action":"CHECK DISCONNECT"},{"caption":"Throttle","action":"1800 RPM"},{"caption":"Ammeter","action":"CHECK"},{"caption":"Engine Instruments","action":"CHECK"},{"caption":"Magnetos","action":"CHECK (125/50)"},{"caption":"Throttle","action":"IDLE CHECK"},{"caption":"Radios","action":"SET"},{"caption":"Brakes","action":"RELEASE"},{"caption":"Door/Windows","action":"CLOSED"},{"caption":"Flaps","action":"AS REQUIRED"},{"caption":"Mixture","action":"RICH","details":"Below 3000 ft"}]},{"id":4,"caption":"Takeoff","items":[{"caption":"Lights","action":"ON AS REQUIRED"},{"caption":"Transponder","action":"ON (ALT)"},{"caption":"Throttle","action":"FULL POWER"},{"caption":"Climb Speed","action":"79 KTS"}]},{"id":5,"caption":"Before landing","items":[{"caption":"Fuel Selector","action":"BOTH"},{"caption":"Engine Gauges","action":"CHECK"},{"caption":"Heading Indicator","action":"ALIGNED"},{"caption":"Altimeter Setting","action":"CHECK"},{"caption":"Radios","action":"SET"},{"caption":"Autopilot","action":"OFF"},{"caption":"Mixture","action":"RICH"},{"caption":"Flaps","action":"DOWN AS REQUIRED"},{"caption":"Approach Speed","action":"65-75 KTS"}]},{"id":6,"caption":"After landing check","items":[{"caption":"Lights (Except Beacon)","action":"OFF"},{"caption":"Transponder","action":"OFF (STAND BY)"},{"caption":"Flaps","action":"UP"},{"caption":"Trim","action":"NEUTRAL"}]},{"id":7,"caption":"Engine shutdown","items":[{"caption":"Throttle","action":"IDLE"},{"caption":"Mags GROUND","action":"CHECK"},{"caption":"Throttle","action":"1000 RPM"},{"caption":"Avionics/Electrical Equip.","action":"OFF"},{"caption":"Mixture","action":"CUTOFF"},{"caption":"Master/Alternator Switch","action":"OFF"},{"caption":"Ignition Switch","action":"OFF"}]},{"id":8,"caption":"Securing aircraft","items":[{"caption":"Control Lock","action":"INSTALL"},{"caption":"Tie-downs/Chocks","action":"INSTALL"},{"caption":"Fuel","action":"RIGHT TANK"}]}]}]
""".trimIndent()

class AircraftBaseImpl : AircraftBase {

    private val base: List<Aircraft>? = try {
//        Json.decodeFromString(loadAircraftJSON("aircraft.json"))
        Json.decodeFromString(DATA)
    } catch (E: Exception) {
        println(E.message)
        null
    }

    override fun getAll(): List<Aircraft> =
        base ?: throw RuntimeException("Can't find files with aircraft!")

    override fun getById(id: Int): Aircraft =
        base?.firstOrNull { it.id == id } ?: throw RuntimeException("Wrong aircraft id!")

    override fun getChecklist(aircraftId: Int, checklistId: Int): Checklist =
        base?.firstOrNull { it.id == aircraftId }?.checklists?.firstOrNull { it.id == checklistId } ?: throw RuntimeException("Wrong checklist id!")
}