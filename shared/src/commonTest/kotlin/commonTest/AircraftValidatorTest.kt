package commonTest

import services.aircraftService.validation.AircraftValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AircraftValidatorTest {

    private val validJson = """
        {
          "id": 7,
          "name": "Test Jet",
          "performance": { "fuelCapacity": 100, "averageCruiseSpeed": 250, "averageFuelFlow": 50 },
          "checklists": [
            {
              "id": 0,
              "caption": "ENGINE START",
              "items": [
                { "caption": "Battery", "action": "ON" },
                { "caption": "LINE" }
              ]
            }
          ],
          "photo": "whatever"
        }
    """.trimIndent()

    @Test
    fun `valid record passes`() {
        val result = AircraftValidator.validate(validJson)
        assertTrue(result.isValid)
        assertNotNull(result.aircraft)
        assertEquals("Test Jet", result.aircraft.name)
    }

    @Test
    fun `malformed JSON is rejected with a syntax message`() {
        val result = AircraftValidator.validate("{ not json")
        assertFalse(result.isValid)
        assertNull(result.aircraft)
        assertTrue(result.errors.single().startsWith("Invalid aircraft file:"))
    }

    @Test
    fun `missing performance is rejected`() {
        val json = validJson.replace(
            """"performance": { "fuelCapacity": 100, "averageCruiseSpeed": 250, "averageFuelFlow": 50 },""",
            ""
        )
        val result = AircraftValidator.validate(json)
        assertFalse(result.isValid)
        assertTrue(result.errors.single().contains("performance"))
    }

    @Test
    fun `missing checklists is rejected`() {
        val json = validJson.substringBefore("\"checklists\"").trimEnd().removeSuffix(",") + "}"
        val result = AircraftValidator.validate(json)
        assertFalse(result.isValid)
        assertTrue(result.errors.single().contains("checklists"))
    }

    @Test
    fun `unknown fields are rejected by strict decoding`() {
        val json = validJson.replace(""""photo": "whatever"""", """"photo": "whatever", "extra": 1""")
        val result = AircraftValidator.validate(json)
        assertFalse(result.isValid)
    }

    @Test
    fun `non-positive performance values are rejected`() {
        val json = validJson.replace(""""fuelCapacity": 100""", """"fuelCapacity": 0""")
        val result = AircraftValidator.validate(json)
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("fuelCapacity") })
    }

    @Test
    fun `duplicate checklist ids are rejected`() {
        val doubled = validJson.replace(
            """"checklists": [""",
            """"checklists": [ { "id": 0, "caption": "TAXI", "items": [ { "caption": "Brakes", "action": "CHECK" } ] },"""
        )
        val result = AircraftValidator.validate(doubled)
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("Duplicate checklist id 0") })
    }

    @Test
    fun `empty item caption or action is rejected`() {
        val noAction = validJson.replace(""""caption": "Battery", "action": "ON"""", """"caption": "Battery", "action": """"")
        assertTrue(AircraftValidator.validate(noAction).errors.any { it.contains("action must not be empty") })

        val noCaption = validJson.replace(""""caption": "Battery", "action": "ON"""", """"caption": "", "action": "ON"""")
        assertTrue(AircraftValidator.validate(noCaption).errors.any { it.contains("caption must not be empty") })
    }

    @Test
    fun `LINE separator item may have an empty action`() {
        val result = AircraftValidator.validate(validJson)
        assertTrue(result.isValid)
    }

    @Test
    fun `unknown category value is rejected`() {
        val json = validJson.replace(
            """"caption": "ENGINE START",""",
            """"caption": "ENGINE START", "category": "EMERGENCY","""
        )
        val result = AircraftValidator.validate(json)
        assertFalse(result.isValid)
    }

    @Test
    fun `valid category values are accepted`() {
        listOf("NORMAL", "NON_NORMAL", "PROCEDURE").forEach { category ->
            val json = validJson.replace(
                """"caption": "ENGINE START",""",
                """"caption": "ENGINE START", "category": "$category","""
            )
            assertTrue(AircraftValidator.validate(json).isValid, "category $category must be accepted")
        }
    }

    @Test
    fun `blank aircraft name is rejected`() {
        val json = validJson.replace(""""name": "Test Jet"""", """"name": "  """")
        val result = AircraftValidator.validate(json)
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("name") })
    }
}
