package feature.qfeHelper

import alexmaryin.metarkt.models.PressureQFE
import feature.qfeHelper.ui.models.QFERunwayUi
import kotlin.math.ceil
import kotlin.math.roundToInt

const val METER_FEET = 3.280839895

data class QFEHelperState(
    val airportICAO: String? = null,
    val airportName: String? = null,
    val runways: List<QFERunwayUi> = emptyList(),
    val selectedRunway: QFERunwayUi? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val elevationMeters: Int = 0,
    val qfe: PressureQFE = PressureQFE.standard(),
    val temperature: Int = 15,
    val heightPlusMeters: Int = 0
) {
    val elevationFeet: Int get() = (elevationMeters * METER_FEET).roundToInt()

    val heightAboveSea: Int
        get() {
            val totalMeters = elevationMeters + heightPlusMeters
            return (ceil(totalMeters * METER_FEET / 10) * 10).roundToInt()
        }
}
