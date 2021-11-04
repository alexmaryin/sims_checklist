package decompose

import androidx.compose.runtime.mutableStateOf
import model.Aircraft
import kotlin.math.roundToInt

class FuelCalculator(
    val aircraft: Aircraft,
    val onBack: () -> Unit
) {
    var tripDistance = mutableStateOf(50f.toString())
    var alterDistance = mutableStateOf(0f.toString())
    var headWindComponent = mutableStateOf("0")
    var blockFuel = mutableStateOf(0)

    val correctInput = mutableStateOf({
        isFloatIncorrect(tripDistance.value).not() && isFloatIncorrect(alterDistance.value).not() &&
                isIntIncorrect(headWindComponent.value).not()
    })

    val fuelExceed = mutableStateOf({
        blockFuel.value > aircraft.performance.fuelCapacity
    })

    fun isFloatIncorrect(value: String, canBeZero: Boolean = true) = with(value.toFloatOrNull()) {
        this == null || (canBeZero && this < 0f) || (canBeZero.not() && this <= 0f)
    }

    fun isIntIncorrect(value: String) = value.toIntOrNull() == null

    private fun fuelInGallons(distance: Float) = with(aircraft.performance) {
        distance / (averageCruiseSpeed - headWindComponent.value.toInt()) * averageFuelFlow
    }.roundToInt()

    private fun contFuel() = (fuelInGallons(tripDistance.value.toFloat()) * aircraft.performance.contingency).roundToInt()

    fun calculateFuel() {
        blockFuel.value = fuelInGallons(tripDistance.value.toFloat()) + fuelInGallons(alterDistance.value.toFloat()) + contFuel()
    }
}