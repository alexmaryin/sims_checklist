package services.aircraftService

import feature.mainScreen.database.AircraftBaseImpl
import kotlinx.coroutines.runBlocking
import repository.AircraftRepositoryImpl
import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RemoveCustomAircraftTest {

    private val customJson = """
        {
          "id": 5,
          "name": "Removable Jet",
          "performance": { "fuelCapacity": 200, "averageCruiseSpeed": 300, "averageFuelFlow": 80 },
          "checklists": [
            { "id": 0, "caption": "ENGINE START", "items": [ { "caption": "Battery", "action": "ON" } ] }
          ],
          "photo": "custom_5"
        }
    """.trimIndent()

    @Test
    fun `removal deletes both files and rewrites the index`() {
        val dir = Files.createTempDirectory("remove-aircraft-test").toFile()
        File(dir, "custom_5.json").writeText(customJson)
        File(dir, "custom_5.jpg").writeBytes(byteArrayOf(1, 2, 3))
        File(dir, "custom_6.json").writeText(customJson.replace("custom_5", "custom_6").replace("\"id\": 5", "\"id\": 6"))
        File(dir, CUSTOM_INDEX).writeText("5\n6")

        removeCustomAircraftFiles(dir, 5)

        assertFalse(File(dir, "custom_5.json").exists())
        assertFalse(File(dir, "custom_5.jpg").exists())
        assertTrue(File(dir, "custom_6.json").exists())
        assertEquals(listOf(6), File(dir, CUSTOM_INDEX).readLines().mapNotNull { it.trim().toIntOrNull() })
    }

    @Test
    fun `removed aircraft disappears from repository and stays gone after reload without warnings`() = runBlocking {
        val dir = Files.createTempDirectory("remove-aircraft-test").toFile()
        File(dir, "custom_5.json").writeText(customJson)
        File(dir, "custom_5.jpg").writeBytes(byteArrayOf(1, 2, 3))
        File(dir, CUSTOM_INDEX).writeText("5")
        val base = AircraftBaseImpl { dir }
        val repository = AircraftRepositoryImpl(base)

        assertEquals(6, repository.getAll().size)

        removeCustomAircraftFiles(dir, 5)
        repository.removeAircraft(5)
        assertEquals(5, repository.getAll().size)
        assertTrue(repository.getAll().none { it.id == 5 })

        // reload from disk: no custom file, no index entry -> no warning, still five aircraft
        val reloaded = assertNotNull(base.getAircraft())
        assertEquals(5, reloaded.size)
        assertTrue(base.loadWarnings.isEmpty())
    }
}
