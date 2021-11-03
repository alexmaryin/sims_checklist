package decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.reduce
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import model.Aircraft
import model.Performance
import kotlin.math.roundToInt

class FuelCalculator(
    aircraft: Aircraft,
    val onBack: () -> Unit
) {
    val state = MutableValue(ComponentData(aircraft.performance))

    fun onTripDistanceChange(value: Float) {
        state.reduce {
            it.copy(tripDistance = value)
        }
    }

    fun onAlterDistanceChange(value: Float) {
        state.reduce {
            it.copy(alterDistance = value)
        }
    }

    fun onHeadWindChange(value: Int) {
        state.reduce {
            it.copy(headWindComponent = value)
        }
    }

    data class ComponentData(
        val performance: Performance,
        val tripDistance: Float = 50f,
        val alterDistance: Float = 0f,
        val headWindComponent: Int = 0,
    ) {
        override fun equals(other: Any?): Boolean {
            return super.equals(other)
        }

        private fun fuelInGallons(distance: Float) = with(performance) {
            distance / (averageCruiseSpeed - headWindComponent) * averageFuelFlow
        }.roundToInt()

        override fun hashCode(): Int {
            var result = performance.hashCode()
            result = 31 * result + tripDistance.hashCode()
            result = 31 * result + alterDistance.hashCode()
            result = 31 * result + headWindComponent
            return result
        }

        val tripFuel get() = fuelInGallons(tripDistance)

        val alterFuel get() = fuelInGallons(alterDistance)

        val contFuel get() = (tripFuel * performance.contingency).roundToInt()

        val blockFuel get() = tripFuel + alterFuel + contFuel
    }
}