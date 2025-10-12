package feature.qfeHelper

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

const val METER_FEET = 3.280839895
const val ONE_BAR = 750.06157584566

data class QFEHelperState(
    val airportICAO: String? = null,
    val airportName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val elevationMeters: Int = 0,
    val qfeMmHg: Int = 760,
    val temperature: Int = 15,
    val heightPlusMeters: Int = 0
) {
    val elevationFeet: Int get() = (elevationMeters * METER_FEET).roundToInt()

    val qfeMilliBar get() = ceil(qfeMmHg / ONE_BAR * 1000).roundToInt()

    val qnh get() = floor(qfeMilliBar + elevationMeters * 0.1171).toInt()

    val heightAboveSea: Int get() {
        val meters = elevationMeters + heightPlusMeters
        // Standard temperature at this elevation (ISA)
        val isaTemp = 15.0 - 0.0065 * meters
        val tempDeviation = temperature - isaTemp
        // Apply temperature correction (~0.4% per 10°C deviation)
        val tempFactor = 1 + (tempDeviation * 0.004)
        return (meters * tempFactor * METER_FEET).roundToInt()
    }
}
