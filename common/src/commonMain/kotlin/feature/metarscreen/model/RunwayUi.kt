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
    val wind: List<WindComponent>? = null
)

data class WindComponent(
    val crossWind: Int = -1,
    val headWind: Int = -1,
    val tailWind: Int = -1
)

fun Runway.toUi() = RunwayUi(
    low = lowNumber,
    high = highNumber,
    lowHeading = lowHeading,
    highHeading = highHeading
)

fun RunwayUi.withCalculatedWind(speedKt: Int, windAngle: Int): RunwayUi {

    fun calculate(heading: Int, wind: Int, speed: Int): WindComponent {
        var angle = abs(heading - wind)
        val tail = angle > 90
        if (tail) angle = abs(180 - angle)
        val corrected = (speed * cos(angle * PI / 180)).toInt()
        val cross = (speed * sin(angle * PI / 180)).toInt()
        return WindComponent(
            crossWind = cross,
            headWind = if(tail) -1 else corrected,
            tailWind = if(tail) corrected else -1
        )
    }

    return copy(
        wind = listOf(lowHeading, highHeading).map {
            calculate(it, windAngle, speedKt)
        }
    )
}

