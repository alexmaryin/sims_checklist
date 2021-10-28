package commonTest

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.Aircraft
import model.AircraftBaseTestImpl
import model.TEST_DATA
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class AircraftBaseTestImplTest {

    private val base = AircraftBaseTestImpl()

    @Test
    fun `get all should return one test aircraft`() {
        assertTrue { base.getAll() == listOf(Json.decodeFromString<Aircraft>(TEST_DATA)) }
    }

    @Test
    fun `get by id != 0 should raise exception`() {
        assertFailsWith<NotImplementedError> { base.getById(1) }
    }

    @Test
    fun `get checklist should return first of its`() {
        assertTrue { base.getChecklist(0, 0).caption == "Preflight" }
    }
}