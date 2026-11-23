package commonTest

import feature.checklistDetails.model.ChecklistCategory
import feature.mainScreen.database.AircraftBaseImpl
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CatalogCategoriesTest {

    private val catalog by lazy { runBlocking { AircraftBaseImpl().getAircraft() } }

    private val expectedAssignments = setOf(
        Triple(2, "ENGINE START", ChecklistCategory.PROCEDURE),
        Triple(3, "ENGINE START", ChecklistCategory.PROCEDURE),
        Triple(4, "RUN UP", ChecklistCategory.PROCEDURE),
        Triple(4, "ENGINE FIRE WHEN STARTING", ChecklistCategory.NON_NORMAL),
        Triple(4, "ENGINE FAILURE IN FLIGHT", ChecklistCategory.NON_NORMAL),
        Triple(4, "ENGINE FIRE IN FLIGHT", ChecklistCategory.NON_NORMAL),
        Triple(4, "LANDING WITHOUT MOTOR", ChecklistCategory.NON_NORMAL),
    )

    @Test
    fun `exactly seven checklists carry explicit non-default categories`() {
        val actual = assertNotNull(catalog).flatMap { aircraft ->
            aircraft.checklists
                .filter { it.category != ChecklistCategory.NORMAL }
                .map { Triple(aircraft.id, it.caption, it.category) }
        }.toSet()
        assertEquals(expectedAssignments, actual)
    }

    @Test
    fun `all other checklists default to NORMAL`() {
        val marked = expectedAssignments.map { it.first to it.second }.toSet()
        assertNotNull(catalog).forEach { aircraft ->
            aircraft.checklists
                .filter { (aircraft.id to it.caption) !in marked }
                .forEach { assertEquals(ChecklistCategory.NORMAL, it.category, "${aircraft.name} / ${it.caption}") }
        }
    }

    @Test
    fun `all checklist captions are uppercase without normal qualifier`() {
        assertNotNull(catalog).flatMap { it.checklists }.forEach { checklist ->
            assertFalse(checklist.caption.any { it.isLowerCase() }, "caption '${checklist.caption}' must be uppercase")
            assertFalse(checklist.caption.contains("(normal)", ignoreCase = true), "caption '${checklist.caption}' must not carry a (normal) qualifier")
        }
    }

    @Test
    fun `all item actions are uppercase`() {
        assertNotNull(catalog).flatMap { it.checklists }.flatMap { it.items }.forEach { item ->
            assertFalse(item.action.any { it.isLowerCase() }, "action '${item.action}' must be uppercase")
        }
    }
}
