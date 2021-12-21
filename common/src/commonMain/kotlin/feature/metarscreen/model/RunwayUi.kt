package feature.metarscreen.model

import services.airportService.model.Runway
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

data class RunwayUi(
    val low: String = "36",
    val high: String = "18",
    val lowHeading: Int = 0,
    val highHeading: Int = 180,
    val wind: RunwayWind? = null
)

fun Runway.toUi() = RunwayUi(
    low = lowNumber,
    high = highNumber,
    lowHeading = lowHeading,
    highHeading = highHeading
)

fun RunwayUi.withCalculatedWind(speedKt: Int, windAngle: Int): RunwayUi {

    fun calculate(heading: Int, wind: Int, speed: Int): Wind {
        var angle = abs(heading - wind)
        val tail = angle > 90
        val rightSide = (heading - wind) in -180..0
        if (tail) angle = abs(180 - angle)
        val corrected = (speed * cos(angle * PI / 180)).toInt()
        val cross = (speed * sin(angle * PI / 180)).toInt()
        return when {
            rightSide && tail -> Wind.RightCrossTailWind(cross, corrected)
            rightSide && tail.not() -> Wind.RightCrossHeadWind(cross, corrected)
            rightSide.not() && tail -> Wind.LeftCrossTailWind(cross, corrected)
            rightSide.not() && tail.not() -> Wind.LeftCrossHeadWind(cross, corrected)
            else -> throw RuntimeException("Wind calculation error")
        }
    }

    return copy(
        wind = RunwayWind(
            lowRunway = calculate(lowHeading, windAngle, speedKt),
            highRunway = calculate(highHeading, windAngle, speedKt)
        )
    )
}

