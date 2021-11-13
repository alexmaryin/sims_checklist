package feature.fuelcalculator

sealed class FuelUiEvent {
    data class TripDistanceChange(val new: String) : FuelUiEvent()
    data class AlterDistanceChange(val new: String) : FuelUiEvent()
    data class HeadwindChange(val new: String) : FuelUiEvent()
    data class CruiseSpeedChange(val new: String) : FuelUiEvent()
    data class FuelFlowChange(val new: String) : FuelUiEvent()
    data class TaxiChange(val new: String) : FuelUiEvent()
    data class ContingencyChange(val new: String) : FuelUiEvent()
    data class ReserveTimeChange(val new: String) : FuelUiEvent()
    data class FuelCapacityChange(val new: String) : FuelUiEvent()
    object SnackBarClose : FuelUiEvent()
    object Back : FuelUiEvent()
}
