package feature.metarscreen.model

import services.airportService.model.Runway
import utils.angleBetweenHeadings
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
        val rightSide = sign(((heading - wind + 540) % 360.0) - 180) <= 0
        val angle = angleBetweenHeadings(heading, wind)
        var corrected = (speed * cos(angle * PI / 180)).roundToInt()
        val cross = abs(speed * sin(angle * PI / 180)).roundToInt()
        val tail = corrected < 0
        corrected = abs(corrected)
        return when {
            rightSide && tail -> Wind.RightCrossTailWind(cross, corrected)
            rightSide -> Wind.RightCrossHeadWind(cross, corrected)
            !rightSide && tail -> Wind.LeftCrossTailWind(cross, corrected)
            else  -> Wind.LeftCrossHeadWind(cross, corrected)
        }
    }

    return copy(
        wind = if(speedKt != 0) RunwayWind(
            lowRunway = calculate(lowHeading, windAngle, speedKt),
            highRunway = calculate(highHeading, windAngle, speedKt)
        ) else null
    )
}

