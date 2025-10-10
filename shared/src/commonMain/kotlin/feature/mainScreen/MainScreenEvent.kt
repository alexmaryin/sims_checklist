package feature.mainScreen

sealed class MainScreenEvent {
    data class SelectAircraft(val aircraftId: Int) : MainScreenEvent()
    data object SelectMetar : MainScreenEvent()
    data object SelectAirportsBase : MainScreenEvent()
    data object SelectQFEHelper : MainScreenEvent()
    data class SelectFuelCalculator(val aircraftId: Int) : MainScreenEvent()
}