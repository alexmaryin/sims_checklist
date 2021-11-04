package decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.reduce
import model.Aircraft
import model.Performance
import kotlin.math.roundToInt

class FuelCalculator(
    val aircraft: Aircraft,
    val onBack: () -> Unit
) {

    val state = MutableValue(Model(aircraft.performance))

    fun isFloatIncorrect(value: String, canBeZero: Boolean = true) = with(value.toFloatOrNull()) {
        this == null || (canBeZero && this < 0f) || (canBeZero.not() && this <= 0f)
    }

    fun isIntIncorrect(value: String) = value.toIntOrNull() == null || value.toInt() >= state.value.performance.averageCruiseSpeed

    fun onTripDistanceChange(new: String) {
        if (isFloatIncorrect(new).not()) state.reduce {
            it.copy(tripDistance = new.toFloat())
        }
    }

    fun onAlterDistanceChange(new: String) {
        if (isFloatIncorrect(new).not()) state.reduce {
            it.copy(alterDistance = new.toFloat())
        }
    }

    fun onHeadwindChange(new: String) {
        if (isIntIncorrect(new).not()) state.reduce {
            it.copy(headWindComponent = new.toInt())
        }
    }

    fun onCruiseSpeedChange(new: String) {
        if (isFloatIncorrect(new, false).not()) state.reduce {
            it.copy(performance = it.performance.copy(
                averageCruiseSpeed = new.toFloat()
            ))
        }
    }

    fun onFuelFlowChange(new: String) {
        if (isFloatIncorrect(new, false).not()) state.reduce {
            it.copy(performance = it.performance.copy(
                averageFuelFlow = new.toFloat()
            ))
        }
    }

    fun onTaxiChange(new: String) {
        if (isFloatIncorrect(new).not()) state.reduce {
            it.copy(performance = it.performance.copy(
                taxiFuel = new.toFloat()
            ))
        }
    }

    fun onContingencyChange(new: String) {
        if (isIntIncorrect(new).not()) state.reduce {
            it.copy(performance = it.performance.copy(
                contingency = new.toInt()
            ))
        }
    }

    fun onReserveTimeChange(new: String) {
        if (isIntIncorrect(new).not()) state.reduce {
            it.copy(performance = it.performance.copy(
                reservesMinutes = new.toInt()
            ))
        }
    }

    fun onFuelCapacityChange(new: String) {
        if (isFloatIncorrect(new).not()) state.reduce {
            it.copy(performance = it.performance.copy(
                fuelCapacity = new.toFloat()
            ))
        }
    }

    data class Model(
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
}