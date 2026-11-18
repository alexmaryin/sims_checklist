package commonTest

import feature.checklistDetails.model.Checklist
import feature.checklistDetails.model.ChecklistCategory
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ChecklistCategoryTest {

    private fun checklistJson(category: String? = null): String {
        val categoryLine = category?.let { ",\n  \"category\": \"$it\"" } ?: ""
        return """
            {
              "id": 0,
              "caption": "ENGINE START",
              "items": [
                { "caption": "Brakes", "action": "TEST & SET" }
              ]$categoryLine
            }
        """.trimIndent()
    }

    @Test
    fun `missing category defaults to NORMAL`() {
        val checklist = Json.decodeFromString<Checklist>(checklistJson())
        assertEquals(ChecklistCategory.NORMAL, checklist.category)
    }

    @Test
    fun `explicit NON_NORMAL category is parsed`() {
        val checklist = Json.decodeFromString<Checklist>(checklistJson("NON_NORMAL"))
        assertEquals(ChecklistCategory.NON_NORMAL, checklist.category)
    }

    @Test
    fun `explicit PROCEDURE category is parsed`() {
        val checklist = Json.decodeFromString<Checklist>(checklistJson("PROCEDURE"))
        assertEquals(ChecklistCategory.PROCEDURE, checklist.category)
    }

    @Test
    fun `explicit NORMAL category is parsed`() {
        val checklist = Json.decodeFromString<Checklist>(checklistJson("NORMAL"))
        assertEquals(ChecklistCategory.NORMAL, checklist.category)
    }
}
