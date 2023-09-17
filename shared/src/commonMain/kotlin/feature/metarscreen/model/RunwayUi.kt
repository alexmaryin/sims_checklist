package feature.metarscreen.model

import services.airportService.model.Runway
import kotlin.math.*

data class RunwayUi(
    val low: String = "",
    val high: String = "",
    val lowHeading: Int = 180,
    val highHeading: Int = 360,
    val wind: RunwayWind? = null
)

fun Runway.toUi() = RunwayUi(
    low = lowNumber,
    high = highNumber,
    lowHeading = lowHeading,
    highHeading = highHeading
)

fun Heading.toRunwayUi(): RunwayUi {
    val lowHeading = if (this < 180 || this == 360) this else this - 180
    val highHeading = if (this >= 180 && this != 360) this else this + 180
    val lowNumber = round(lowHeading / 10.0).toString()
    val highNumber = round(highHeading / 10.0).toString()
    return RunwayUi(lowNumber, highNumber, lowHeading, highHeading)
}

fun RunwayUi.withCalculatedWind(speedKt: Int, windAngle: Int): RunwayUi {

    fun calculate(heading: Int, wind: Int, speed: Int): Wind {
        var angle = abs(heading - wind)
        val tail = angle > 90
        val rightSide = (heading - wind) in -180..0
        if (tail) angle = abs(180 - angle)
        val corrected = (speed * cos(angle * PI / 180)).toInt().coerceAtLeast(0)
        val cross = (speed * sin(angle * PI / 180)).toInt().coerceAtLeast(0)
        return when {
            rightSide && tail -> Wind.RightCrossTailWind(cross, corrected)
            rightSide && tail.not() -> Wind.RightCrossHeadWind(cross, corrected)
            rightSide.not() && tail -> Wind.LeftCrossTailWind(cross, corrected)
            rightSide.not() && tail.not() -> Wind.LeftCrossHeadWind(cross, corrected)
            else -> throw RuntimeException("Wind calculation error")
        }
    }

    return copy(
        wind = if(windAngle != 0 && speedKt != 0) RunwayWind(
            lowRunway = calculate(lowHeading, windAngle, speedKt),
            highRunway = calculate(highHeading, windAngle, speedKt)
        ) else null
    )
}

