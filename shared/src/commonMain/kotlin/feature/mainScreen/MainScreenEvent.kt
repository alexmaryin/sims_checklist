package feature.mainScreen

sealed class MainScreenEvent {
    data object ClearSnack : MainScreenEvent()
    data object StartUpdate : MainScreenEvent()
    data object DropBaseConfirm : MainScreenEvent()
    data object DropBase : MainScreenEvent()
    data object OpenToolsMenu : MainScreenEvent()
    data class SelectAircraft(val aircraftId: Int) : MainScreenEvent()
    data object SelectMetar : MainScreenEvent()
    data object SelectAirportsBase : MainScreenEvent()
    data object SelectQFEHelper : MainScreenEvent()
    data object SelectColdTemperature : MainScreenEvent()
    data object SelectAddCustomAircraft : MainScreenEvent()
    data class RemoveAircraftConfirm(val aircraftId: Int) : MainScreenEvent()
    data class RemoveAircraft(val aircraftId: Int) : MainScreenEvent()
    data class SelectFuelCalculator(val aircraftId: Int) : MainScreenEvent()
}
