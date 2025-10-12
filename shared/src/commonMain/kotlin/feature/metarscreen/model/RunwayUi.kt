package feature.metarscreen.model

import alexmaryin.metarkt.helpers.componentForRunwayTrue
import alexmaryin.metarkt.models.Wind
import services.airportService.model.Runway
import kotlin.math.abs
import kotlin.math.roundToInt

data class RunwayUi(
    val low: String = "18",
    val high: String = "36",
    val lowHeading: Heading = 180,
    val highHeading: Heading = 360,
    val wind: RunwayWindUi? = null
)

fun Runway.toUi() = RunwayUi(
    low = lowNumber,
    high = highNumber,
    lowHeading = lowHeading,
    highHeading = highHeading
)

@Suppress("DefaultLocale")
fun Heading.toRunwayUi(): RunwayUi {

    val lowHeading = if (this <= 180) this else this - 180
    val highHeading = lowHeading + 180

    fun Int.toRunwayNumber(): String {
        val number = (this / 10.0).roundToInt()
        val correctedNumber = if (number == 0) 36 else number
        return String.format("%02d", correctedNumber)
    }

    return RunwayUi(
        low = lowHeading.toRunwayNumber(),
        high = highHeading.toRunwayNumber(),
        lowHeading = lowHeading,
        highHeading = highHeading
    )
}

fun RunwayUi.withCalculatedWind(wind: Wind): RunwayUi {

    fun calculate(heading: Heading): WindUi {
        val component = wind.componentForRunwayTrue(heading)
        val cross = component.crosswind.roundToInt()
        val tailOrHead = abs(component.headwind.roundToInt())

        return when {
            component.fromLeft -> when {
                component.headwind < 0 -> WindUi.LeftCrossTailWindUi(cross, tailOrHead)
                else -> WindUi.LeftCrossHeadWindUi(cross, tailOrHead)
            }
            else -> when { // from right
                component.headwind < 0 -> WindUi.RightCrossTailWindUi(cross, tailOrHead)
                else -> WindUi.RightCrossHeadWindUi(cross, tailOrHead)
            }
        }
    }

    if (wind.speedKt == 0) return this

    return copy(
        wind = RunwayWindUi(
            lowRunway = calculate(lowHeading),
            highRunway = calculate(highHeading)
        )
    )
}
