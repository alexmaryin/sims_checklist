package feature.fuelcalculator

import feature.fuelcalculator.model.Performance
import kotlin.math.roundToInt

data class FuelCalcViewState(
    val name: String,
    val performance: Performance,
    val tripDistance: Float = 100f,
    val alterDistance: Float = 0f,
    val headWindComponent: Int = 0,
) {
    private fun fuelInGallons(distance: Float) = with(performance) {
        distance / (averageCruiseSpeed - headWindComponent) * averageFuelFlow
    }.roundToInt()

    private fun contFuel() = fuelInGallons(tripDistance * performance.contingency / 100)

    private fun reserveFuel() = performance.reservesMinutes / 60 * performance.averageFuelFlow

    fun blockFuel() = fuelInGallons(tripDistance) + fuelInGallons(alterDistance) + contFuel() + performance.taxiFuel + reserveFuel()

    val fuelExceed get() = blockFuel() > performance.fuelCapacity
}

sealed class FuelSnackBarEvent(
    val message: String,
    val button: String,
) {
    data class Error(val error: String) : FuelSnackBarEvent(
        message = error,
        button = "Close",
    )
}