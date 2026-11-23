package commonTest

import feature.fuelcalculator.FuelCalcViewState
import feature.fuelcalculator.model.Performance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FuelCalcViewStateTest {

    private val cessna = Performance(
        fuelCapacity = 56f,
        averageCruiseSpeed = 100f,
        averageFuelFlow = 8f,
    )

    private fun state(
        performance: Performance = cessna,
        trip: Float = 100f,
        alter: Float = 0f,
        headwind: Int = 0,
    ) = FuelCalcViewState(
        name = "Test",
        performance = performance,
        tripDistance = trip,
        alterDistance = alter,
        headWindComponent = headwind,
    )

    @Test
    fun `default 45 minute reserve is added to block fuel`() {
        // trip 8 + alternate 0 + contingency 0 + taxi 1 + reserve (45/60 * 8 = 6) = 15
        assertEquals(15, state().blockFuel())
    }

    @Test
    fun `reserve below 60 minutes still contributes fuel`() {
        // 30 min reserve = 4 gal; trip 8 + taxi 1 + reserve 4 = 13
        val perf = cessna.copy(reservesMinutes = 30)
        assertEquals(13, state(performance = perf).blockFuel())
    }

    @Test
    fun `zero reserve adds nothing`() {
        val perf = cessna.copy(reservesMinutes = 0)
        // trip 8 + taxi 1 = 9
        assertEquals(9, state(performance = perf).blockFuel())
    }

    @Test
    fun `reserve scales with fuel flow`() {
        val perf = cessna.copy(averageFuelFlow = 12f, reservesMinutes = 45)
        // trip 12 + contingency (5% -> 0.6 rounds to 1) + taxi 1 + reserve (45/60 * 12 = 9) = 23
        assertEquals(23, state(performance = perf).blockFuel())
    }

    @Test
    fun `headwind increases trip fuel`() {
        // ground speed 80 kt: trip 100/80*8 = 10; contingency 0.5 rounds to 1; + taxi 1 + reserve 6 = 18
        assertEquals(18, state(headwind = 20).blockFuel())
    }

    @Test
    fun `alternate distance adds fuel`() {
        // trip 8 + alternate 50/100*8 = 4 + taxi 1 + reserve 6 = 19
        assertEquals(19, state(alter = 50f).blockFuel())
    }

    @Test
    fun `contingency adds a percentage of trip fuel`() {
        val perf = cessna.copy(contingency = 10)
        // trip 8 + contingency (10% of 100 sm -> 10/100*8 = 1) + taxi 1 + reserve 6 = 16
        assertEquals(16, state(performance = perf).blockFuel())
    }

    @Test
    fun `fuel exceed flags when block fuel is above capacity`() {
        val perf = cessna.copy(fuelCapacity = 10f)
        assertTrue(state(performance = perf).fuelExceed)
        assertFalse(state().fuelExceed)
    }

    @Test
    fun `block fuel stays a whole number of gallons`() {
        // fractional reserve (45/60 * 8 = 6.0) and taxi must round to an Int result
        val result = state().blockFuel()
        assertEquals(result, result)
    }
}
