package decompose

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import feature.airportsBase.AirportsBase
import feature.checklistDetails.ChecklistDetails
import feature.checklists.Checklists
import feature.fuelcalculator.FuelCalculator
import feature.metarscreen.MetarScanner

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked(toIndex: Int)

    sealed class Child {
        class AircraftListChild(val component: AircraftList): Child()
        class ChecklistsChild(val component: Checklists): Child()
        class ChecklistDetailsChild(val component: ChecklistDetails): Child()
        class FuelCalculatorChild(val component: FuelCalculator): Child()
        class MetarScannerChild(val component: MetarScanner): Child()
        class AirportsBaseChild(val component: AirportsBase): Child()
    }
}