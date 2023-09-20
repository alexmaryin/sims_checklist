package feature.metarscreen.model

import services.airportService.model.Runway
import kotlin.math.*

data class RunwayUi(
    val low: String = "18",
    val high: String = "36",
    val lowHeading: Heading = 180,
    val highHeading: Heading = 360,
    val wind: RunwayWind? = null
)

fun Runway.toUi() = RunwayUi(
    low = lowNumber,
    high = highNumber,
    lowHeading = lowHeading,
    highHeading = highHeading
)

fun Heading.toRunwayUi(): RunwayUi {

    fun Int.checkZero() = if (this != 0) this else 36

    val lowHeading = (if (this <= 180) this else this - 180)
    val highHeading = (if (this > 180) this else this + 180)
    val lowNumber = String.format("%02d", (lowHeading / 10.0).roundToInt().checkZero())
    val highNumber = String.format("%02d", (highHeading / 10.0).roundToInt())
    return RunwayUi(lowNumber, highNumber, lowHeading, highHeading)
}

fun RunwayUi.withCalculatedWind(speedKt: Int, windAngle: Heading): RunwayUi {

    fun calculate(heading: Heading, wind: Heading, speed: Int): Wind {
        val correctedHeading = if (heading == 360) 0 else heading
        var angle = abs(correctedHeading - wind)
        val tail = angle > 90
        val rightSide = sign(((correctedHeading - wind + 540) % 360.0) - 180) <= 0
        if (tail) angle = abs(180 - angle)
        val corrected = (speed * cos(angle * PI / 180)).roundToInt().coerceAtLeast(0)
        val cross = (speed * sin(angle * PI / 180)).roundToInt().coerceAtLeast(0)
        return when {
            rightSide && tail -> Wind.RightCrossTailWind(cross, corrected)
            rightSide -> Wind.RightCrossHeadWind(cross, corrected)
            !rightSide && tail -> Wind.LeftCrossTailWind(cross, corrected)
            else  -> Wind.LeftCrossHeadWind(cross, corrected)
        }
    }

    return copy(
        wind = if(windAngle != 0 && speedKt != 0) RunwayWind(
            lowRunway = calculate(lowHeading, windAngle, speedKt),
            highRunway = calculate(highHeading, windAngle, speedKt)
        ) else null
    )
}

