package feature.mainScreen.database

import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AircraftBaseImplTest {

    private val validCustomJson = """
        {
          "id": 5,
          "name": "Custom Jet",
          "performance": { "fuelCapacity": 200, "averageCruiseSpeed": 300, "averageFuelFlow": 80 },
          "checklists": [
            { "id": 0, "caption": "ENGINE START", "items": [ { "caption": "Battery", "action": "ON" } ] }
          ],
          "photo": "custom_5"
        }
    """.trimIndent()

    private fun newBase(): Pair<AircraftBaseImpl, File> {
        val dir = Files.createTempDirectory("aircraft-base-test").toFile()
        return AircraftBaseImpl { dir } to dir
    }

    @Test
    fun `first load deploys bundled files and returns five aircraft`() = runBlocking {
        val (base, dir) = newBase()
        val catalog = assertNotNull(base.getAircraft())
        assertEquals(listOf(0, 1, 2, 3, 4), catalog.map { it.id })
        assertTrue(File(dir, "cessna172.json").exists())
        assertTrue(File(dir, ".deployed-version").exists())
        assertTrue(base.loadWarnings.isEmpty())
    }

    @Test
    fun `deleted app file falls back to the bundled resource`() = runBlocking {
        val (base, dir) = newBase()
        base.getAircraft()
        assertTrue(File(dir, "cessna172.json").delete())

        val catalog = assertNotNull(base.getAircraft())
        assertEquals(5, catalog.size)
        assertTrue(catalog.any { it.id == 0 })
        assertTrue(base.loadWarnings.isEmpty())
    }

    @Test
    fun `corrupt app file falls back to the bundled resource`() = runBlocking {
        val (base, dir) = newBase()
        base.getAircraft()
        File(dir, "citationx.json").writeText("{ definitely not json")

        val catalog = assertNotNull(base.getAircraft())
        assertEquals(5, catalog.size)
        assertEquals("Cessna Citation X", catalog.first { it.id == 2 }.name)
        assertTrue(base.loadWarnings.isEmpty())
    }

    @Test
    fun `valid custom aircraft is loaded alongside bundled ones`() = runBlocking {
        val (base, dir) = newBase()
        base.getAircraft()
        File(dir, "custom_5.json").writeText(validCustomJson)

        val catalog = assertNotNull(base.getAircraft())
        assertEquals(6, catalog.size)
        assertEquals("Custom Jet", catalog.first { it.id == 5 }.name)
        assertTrue(base.loadWarnings.isEmpty())
    }

    @Test
    fun `invalid custom file is skipped with a warning`() = runBlocking {
        val (base, dir) = newBase()
        base.getAircraft()
        File(dir, "custom_9.json").writeText("{ broken")

        val catalog = assertNotNull(base.getAircraft())
        assertEquals(5, catalog.size)
        assertTrue(base.loadWarnings.any { it.contains("custom_9.json") })
    }

    @Test
    fun `missing indexed custom file is skipped with a warning`() = runBlocking {
        val (base, dir) = newBase()
        base.getAircraft()
        File(dir, ".custom-index").writeText("7\n")

        val catalog = assertNotNull(base.getAircraft())
        assertEquals(5, catalog.size)
        assertTrue(base.loadWarnings.any { it.contains("custom_7") && it.contains("missing") })
    }

    @Test
    fun `one bad file does not break the rest of the catalog`() = runBlocking {
        val (base, dir) = newBase()
        base.getAircraft()
        File(dir, "king_air_350.json").writeText("garbage")
        File(dir, "custom_5.json").writeText(validCustomJson)

        val catalog = assertNotNull(base.getAircraft())
        assertEquals(6, catalog.size)
        assertTrue(catalog.map { it.id }.containsAll(listOf(0, 1, 2, 3, 4, 5)))
        assertTrue(base.loadWarnings.isEmpty(), "bundled fallback must not warn")
    }
}
