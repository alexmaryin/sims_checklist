package feature.metarscreen.model

import kotlin.test.Test
import kotlin.test.assertEquals

internal class RunwayUiKtTest {

    @Test
    fun `toRunwayUi should return correct number for heading 360`() {
        val runway = 360.toRunwayUi()
        val reference = RunwayUi(
            low = "18",
            high = "36",
            lowHeading = 180,
            highHeading = 360
        )
        assertEquals(expected = reference, actual = runway)
    }

    @Test
    fun `toRunwayUi should return correct number for heading 5`() {
        val runway = 5.toRunwayUi()
        val reference = RunwayUi(
            low = "36",
            high = "18",
            lowHeading = 5,
            highHeading = 185
        )
        assertEquals(expected = reference, actual = runway)
    }
}