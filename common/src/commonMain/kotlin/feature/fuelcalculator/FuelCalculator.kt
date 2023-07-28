package feature.fuelcalculator

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import feature.checklists.model.Aircraft

class FuelCalculator(
    aircraft: Aircraft,
    private val onBack: () -> Unit
) {

    val state = MutableValue(FuelCalcViewState(aircraft.name, aircraft.performance))

    fun isFloatIncorrect(value: String, canBeZero: Boolean = true) = with(value.toFloatOrNull()) {
        this == null || (canBeZero && this < 0f) || (canBeZero.not() && this <= 0f)
    }

    fun isIntIncorrect(value: String, maxValue: Int = 100) =
        value.toIntOrNull() == null || value.toInt() >= maxValue

    fun onEvent(event: FuelUiEvent) {
        when (event) {
            is FuelUiEvent.TripDistanceChange -> onTripDistanceChange(event.new)
            is FuelUiEvent.AlterDistanceChange -> onAlterDistanceChange(event.new)
            is FuelUiEvent.HeadwindChange -> onHeadwindChange(event.new)
            is FuelUiEvent.CruiseSpeedChange -> onCruiseSpeedChange(event.new)
            is FuelUiEvent.FuelFlowChange -> onFuelFlowChange(event.new)
            is FuelUiEvent.TaxiChange -> onTaxiChange(event.new)
            is FuelUiEvent.ContingencyChange -> onContingencyChange(event.new)
            is FuelUiEvent.ReserveTimeChange -> onReserveTimeChange(event.new)
            is FuelUiEvent.FuelCapacityChange -> onFuelCapacityChange(event.new)
            is FuelUiEvent.SnackBarClose -> state.update { it.copy(snackBar = null) }
            is FuelUiEvent.Back -> onBack()
        }
    }


    private fun onTripDistanceChange(new: String) {
        if (isFloatIncorrect(new).not()) state.update {
            it.copy(tripDistance = new.toFloat(), snackBar = null)
        } else {
            state.update {
                it.copy(snackBar = FuelSnackBarState.ErrorHint("Only digits are allowed in trip distance"))
            }
        }
    }


    private fun onAlterDistanceChange(new: String) {
        if (isFloatIncorrect(new).not()) state.update {
            it.copy(alterDistance = new.toFloat(), snackBar = null)
        } else {
            state.update {
                it.copy(snackBar = FuelSnackBarState.ErrorHint("Only digits are allowed in alter. distance"))
            }
        }
    }

    private fun onHeadwindChange(new: String) {
        if (isIntIncorrect(new, state.value.performance.averageCruiseSpeed.toInt()).not()) state.update {
            it.copy(headWindComponent = new.toInt(), snackBar = null)
        } else {
            state.update {
                it.copy(snackBar = FuelSnackBarState.ErrorHint("Only digits are allowed in headwind and not exceed the speed of aircraft"))
            }
        }
    }

    private fun onCruiseSpeedChange(new: String) {
        if (isFloatIncorrect(new, false).not()) state.update {
            it.copy(
                performance = it.performance.copy(averageCruiseSpeed = new.toFloat()),
                snackBar = null
            )
        } else {
            state.update {
                it.copy(snackBar = FuelSnackBarState.ErrorHint("Only digits are allowed in cruise speed"))
            }
        }
    }

    private fun onFuelFlowChange(new: String) {
        if (isFloatIncorrect(new, false).not()) state.update {
            it.copy(
                performance = it.performance.copy(averageFuelFlow = new.toFloat()),
                snackBar = null
            )
        } else {
            state.update {
                it.copy(snackBar = FuelSnackBarState.ErrorHint("Only digits are allowed in fuel flow"))
            }
        }
    }

    private fun onTaxiChange(new: String) {
        if (isFloatIncorrect(new).not()) state.update {
            it.copy(
                performance = it.performance.copy(taxiFuel = new.toFloat()),
                snackBar = null
            )
        } else {
            state.update {
                it.copy(snackBar = FuelSnackBarState.ErrorHint("Only digits are allowed in taxi"))
            }
        }
    }

    private fun onContingencyChange(new: String) {
        if (isIntIncorrect(new, 100).not()) state.update {
            it.copy(
                performance = it.performance.copy(contingency = new.toInt()),
                snackBar = null
            )
        } else {
            state.update {
                it.copy(snackBar = FuelSnackBarState.ErrorHint("Only digits are allowed in contingency and not exceed 100%"))
            }
        }
    }

    private fun onReserveTimeChange(new: String) {
        if (isIntIncorrect(new, 90).not()) state.update {
            it.copy(
                performance = it.performance.copy(reservesMinutes = new.toInt()),
                snackBar = null
            )
        } else {
            state.update {
                it.copy(snackBar = FuelSnackBarState.ErrorHint("Only digits are allowed in reserve time and not exceed 90 min"))
            }
        }
    }

    private fun onFuelCapacityChange(new: String) {
        if (isFloatIncorrect(new).not()) state.update {
            it.copy(
                performance = it.performance.copy(fuelCapacity = new.toFloat()),
                snackBar = null
            )
        } else {
            state.update {
                it.copy(snackBar = FuelSnackBarState.ErrorHint("Only digits are allowed in fuel capacity"))
            }
        }
    }
}