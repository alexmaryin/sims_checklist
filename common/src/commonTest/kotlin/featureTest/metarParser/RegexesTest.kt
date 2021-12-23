package featureTest.metarParser

import feature.metarParser.MetarParser
import feature.metarParser.Wind
import feature.metarParser.WindUnit
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue

internal class RegexesTest {

    private val metarExamples = """
        UWLL 231100Z 20002MPS 9999 BKN030 M22/M26 Q1012 R20/820245 NOSIG RMK QFE749/0999
        KLAX 231053Z 35005KT 10SM FEW006 BKN040 OVC070 14/11 A3003 RMK AO2 RAB05E53 SLP168 P0000 T01390111
        LOWI 231120Z VRB02KT CAVOK 00/M07 Q1018 NOSIG
        ULLI 231100Z 24007G12MPS 200V290 9000 -SHSN DRSN BKN014CB OVC032 M03/M06 Q0998 RESHSN R28L/491037 R28R/451040 TEMPO 22010G16MPS 1000 SHSN BLSN BKN007 BKN020CB
        UWWW 231100Z VRB01MPS CAVOK M20/M25 Q1012 R33/450250 NOSIG RMK QFE748/0998
        EGLL 231120Z AUTO 20009KT 6000 OVC004 08/07 Q1008 TEMPO BKN005
    """.trimIndent().split("\n")

    @Test
    fun `Parser should find all station names at start`() {
        val stations = metarExamples.mapNotNull { raw ->
            MetarParser(raw).parse().station
        }
        assertTrue { stations == listOf("UWLL", "KLAX", "LOWI", "ULLI", "UWWW", "EGLL") }
    }

    @Test
    fun `Parser should find correct time of report`() {
        val reports = metarExamples.mapNotNull { raw ->
            MetarParser(raw).parse().reportTime
        }
        assertTrue { reports == listOf(
            LocalDateTime.parse("2021-12-23T11:00:00"),
            LocalDateTime.parse("2021-12-23T10:53:00"),
            LocalDateTime.parse("2021-12-23T11:20:00"),
            LocalDateTime.parse("2021-12-23T11:00:00"),
            LocalDateTime.parse("2021-12-23T11:00:00"),
            LocalDateTime.parse("2021-12-23T11:20:00"),
        ) }
    }

    @Test
    fun `Parser should find correct wind information`() {
        val winds = metarExamples.mapNotNull { raw ->
            MetarParser(raw).parse().wind
        }
        assertTrue { winds == listOf(
            Wind(direction = 200, speed = 2, speedUnits = WindUnit.MPS),
            Wind(direction = 350, speed = 5, speedUnits = WindUnit.KT),
            Wind(variable = true, speed = 2, speedUnits = WindUnit.KT),
            Wind(direction = 240, speed = 7, speedUnits = WindUnit.MPS, gusts = 12),
            Wind(variable = true, speed = 1, speedUnits = WindUnit.MPS),
            Wind(direction = 200, speed = 9, speedUnits = WindUnit.KT),
        ) }
    }
}