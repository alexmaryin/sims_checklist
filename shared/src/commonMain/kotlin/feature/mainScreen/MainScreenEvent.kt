package feature.mainScreen

sealed class MainScreenEvent {
    data object ClearSnack : MainScreenEvent()
    data object StartUpdate : MainScreenEvent()
    data object DropBase : MainScreenEvent()
    data class SelectAircraft(val aircraftId: Int) : MainScreenEvent()
    data object SelectMetar : MainScreenEvent()
    data object SelectAirportsBase : MainScreenEvent()
    data object SelectQFEHelper : MainScreenEvent()
    data class SelectFuelCalculator(val aircraftId: Int) : MainScreenEvent()
}