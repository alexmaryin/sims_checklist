package commonTest

import feature.checklistDetails.model.CHECKLIST_LINE
import feature.mainScreen.database.AircraftBaseImpl
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class CitationXChecklistsTest {

    private val expectedCaptions = listOf(
        "Preflight", "Pre start", "Before engine start", "Engine start",
        "After start / Taxi", "Before takeoff", "Climb", "Cruise",
        "Descent", "Approach", "After landing", "Parking", "Shutdown",
    )

    private val catalog by lazy { runBlocking { AircraftBaseImpl().getAircraft() } }

    private val citationX: feature.checklists.model.Aircraft
        get() = assertNotNull(catalog).first { it.id == 2 }

    @Test
    fun `catalog parses with all aircraft`() {
        assertNotNull(catalog, "bundled aircraft.json must deserialize")
        assertEquals(listOf(0, 1, 2, 3, 4), catalog!!.map { it.id })
    }

    @Test
    fun `citation X identity fields are retained`() {
        assertEquals("Cessna Citation X", citationX.name)
        assertEquals("citationx", citationX.photo)
        assertEquals(1930f, citationX.performance.fuelCapacity)
        assertEquals(528f, citationX.performance.averageCruiseSpeed)
        assertEquals(336f, citationX.performance.averageFuelFlow)
    }

    @Test
    fun `citation X has thirteen checklists in order`() {
        val checklists = citationX.checklists
        assertEquals(expectedCaptions.size, checklists.size)
        assertEquals((0..12).toList(), checklists.map { it.id })
        assertEquals(expectedCaptions, checklists.map { it.caption })
    }

    @Test
    fun `citation X omits sim setup and roll procedures`() {
        val checklists = citationX.checklists
        assertTrue(checklists.none { it.caption == "FMS setup" })
        val items = checklists.flatMap { it.items.map { item -> item.caption } }
        listOf("Taxi speed", "Approach", "Landing check", "Landing roll", "Go-around")
            .forEach { absent -> assertFalse(items.contains(absent), "'$absent' must be removed") }
    }

    @Test
    fun `citation X uses no LINE separators`() {
        citationX.checklists.forEach { checklist ->
            assertTrue(checklist.items.none { it.caption == CHECKLIST_LINE },
                "checklist '${checklist.caption}' must not contain LINE items")
        }
    }

    @Test
    fun `citation X drops superseded terminology`() {
        val text = citationX.checklists.joinToString("\n") { checklist ->
            checklist.items.joinToString("\n") { "${it.caption} ${it.action} ${it.details}" }
        }
        listOf("PACS", "hold 5 sec", "Vref... 132", "145 Kt", "Replay").forEach { banned ->
            assertFalse(text.contains(banned), "obsolete wording '$banned' must be gone")
        }
    }

    @Test
    fun `reference figures are kept as details on relevant items`() {
        val vSpeeds = citationX.checklists.first { it.caption == "Before engine start" }
            .items.first { it.caption == "PFD V-Speeds" }
        assertContains(vSpeeds.details, "V1...114 / 116 / 125 / 133")
        assertContains(vSpeeds.details, "VR...116 / 119 / 128 / 134")

        val stabTrim = citationX.checklists.first { it.caption == "Before takeoff" }
            .items.first { it.caption == "Stab trim" }
        assertContains(stabTrim.details, "Flaps 15°...-8.0 / -6.9 / -5.8 / -4.6 / -3.5")

        val vref = citationX.checklists.first { it.caption == "Approach" }
            .items.first { it.caption == "VREF / VAPP" }
        assertContains(vref.details, "VAPP...113 / 116 / 121 / 126 / 131 / 136")
    }
}
