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
            low = "01",
            high = "19",
            lowHeading = 5,
            highHeading = 185
        )
        assertEquals(expected = reference, actual = runway)
    }

    @Test
    fun `toRunwayUi should return correct number for heading 25`() {
        val runway = 25.toRunwayUi()
        val reference = RunwayUi(
            low = "03",
            high = "21",
            lowHeading = 25,
            highHeading = 205
        )
        assertEquals(expected = reference, actual = runway)
    }

    @Test
    fun `toRunwayUi should return correct number for heading 180`() {
        val runway = 180.toRunwayUi()
        val reference = RunwayUi(
            low = "18",
            high = "36",
            lowHeading = 180,
            highHeading = 360
        )
        assertEquals(expected = reference, actual = runway)
    }

    @Test
    fun `toRunwayUi should return correct number for heading 226`() {
        val runway = 226.toRunwayUi()
        val reference = RunwayUi(
            low = "05",
            high = "23",
            lowHeading = 46,
            highHeading = 226
        )
        assertEquals(expected = reference, actual = runway)
    }

    @Test
    fun `toRunwayUi should return correct number for heading 335`() {
        val runway = 335.toRunwayUi()
        val reference = RunwayUi(
            low = "16",
            high = "34",
            lowHeading = 155,
            highHeading = 335
        )
        assertEquals(expected = reference, actual = runway)
    }

    @Test
    fun `calculateWind should return correct data for runway 18 and crosswind 15006Kt`() {
        val result = RunwayUi("18", "36", 180, 360)
            .withCalculatedWind(5, 150)
        val reference = RunwayUi("18", "36", 180, 360,
            RunwayWindUi(
                lowRunway = WindUi.LeftCrossHeadWindUi(2, 4),
                highRunway = WindUi.RightCrossTailWindUi(2, 4)
            )
        )
        assertEquals(expected = reference, actual = result)
    }

    @Test
    fun `calculateWind should return correct data for runway 22 and crosswind 03012Kt`() {
        val result = RunwayUi("22", "04", 35, 215)
            .withCalculatedWind(12, 30)
        val reference = RunwayUi("22", "04", 35, 215,
            RunwayWindUi(
                lowRunway = WindUi.LeftCrossHeadWindUi(1, 12),
                highRunway = WindUi.RightCrossTailWindUi(1, 12)
            )
        )
        assertEquals(expected = reference, actual = result)
    }

    @Test
    fun `calculateWind should return correct data for runway 10 and crosswind 15003Kt`() {
        val result = RunwayUi("10", "28", 100, 280)
            .withCalculatedWind(3, 150)
        val reference = RunwayUi("10", "28", 100, 280,
            RunwayWindUi(
                lowRunway = WindUi.RightCrossHeadWindUi(2, 2),
                highRunway = WindUi.LeftCrossTailWindUi(2, 2)
            )
        )
        assertEquals(expected = reference, actual = result)
    }
}