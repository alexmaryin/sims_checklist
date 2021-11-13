package feature.fuelcalculator

import feature.fuelcalculator.model.Performance
import kotlin.math.roundToInt

data class FuelCalcViewState(
    val name: String,
    val performance: Performance,
    val tripDistance: Float = 100f,
    val alterDistance: Float = 0f,
    val headWindComponent: Int = 0,
    val snackBar: FuelSnackBarState? = null
) {
    private fun fuelInGallons(distance: Float) = with(performance) {
        distance / (averageCruiseSpeed - headWindComponent) * averageFuelFlow
    }.roundToInt()

    private fun contFuel() = fuelInGallons(tripDistance * performance.contingency / 100)

    private fun reserveFuel() = performance.reservesMinutes / 60 * performance.averageFuelFlow

    fun blockFuel() = fuelInGallons(tripDistance) + fuelInGallons(alterDistance) + contFuel() + performance.taxiFuel + reserveFuel()

    val fuelExceed get() = blockFuel() > performance.fuelCapacity
}

sealed class FuelSnackBarState(
    val message: String,
    val button: String,
    val event: FuelUiEvent
) {
    data class ErrorHint(val error: String) : FuelSnackBarState(
        message = error,
        button = "Close",
        event = FuelUiEvent.SnackBarClose
    )
}