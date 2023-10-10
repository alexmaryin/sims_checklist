package feature.qfeHelper

import kotlin.math.roundToInt

const val METER_FEET = 3.280839895
const val ONE_BAR = 750.06157584566
data class QFEHelperState(
    val airportICAO: String = "",
    val airportName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val elevationMeters: Int = 0,
    val elevationFeet: Int = 0,
    val qfeMmHg: Int = 760,
    val qfeMilliBar: Int = 1013,
    val qnh: Int = 1013,
    val heightPlusMeters: Int = 0
) {
    fun elevationFeet(meters: Int) = (meters * METER_FEET).roundToInt()
    fun qfeMilliBar(mmHg: Int) = (mmHg / ONE_BAR).roundToInt() * 1000
    fun qnh() = elevationFeet / 30 + qfeMilliBar
}
