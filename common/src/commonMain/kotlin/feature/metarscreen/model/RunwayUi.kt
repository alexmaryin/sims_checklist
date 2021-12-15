package feature.metarscreen.model

import services.airportService.model.Runway

data class RunwayUi(
    val low: String = "36",
    val high: String = "18",
    val angle: Int = 0
) {
    val oppositeAngle get() = if(angle < 180) angle + 180 else 360 - angle
}

fun Runway.toUi() = RunwayUi(
    low = lowNumber,
    high = highNumber,
    angle = lowHeading
)
