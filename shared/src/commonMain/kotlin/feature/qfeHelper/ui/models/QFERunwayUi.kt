package feature.qfeHelper.ui.models

import feature.qfeHelper.METER_FEET
import services.airportService.model.Runway
import kotlin.math.roundToInt

data class QFERunwayUi(
    val number: String,
    val elevationMeters: Int,
    val elevationFt: Int
) {
    companion object {
        fun fromMeters(number: String, meters: Int) = QFERunwayUi(
            number,
            meters,
            (meters * METER_FEET).roundToInt()
        )

        fun fromFeet(number: String, feet: Int) = QFERunwayUi(
            number,
            (feet / METER_FEET).roundToInt(),
            feet
        )
    }
}

fun Runway.toUi(defaultElevationFeet: Int) = listOf(
    QFERunwayUi.fromFeet(lowNumber, lowElevationFeet ?: defaultElevationFeet),
    QFERunwayUi.fromFeet(highNumber, highElevationFeet ?: defaultElevationFeet)
)
