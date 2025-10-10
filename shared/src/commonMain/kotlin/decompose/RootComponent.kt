package decompose

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import feature.airportsBase.AirportsBase
import feature.checklistDetails.ChecklistDetails
import feature.checklists.Checklists
import feature.fuelcalculator.FuelCalculator
import feature.mainScreen.MainEventExecutor
import feature.metarscreen.MetarScanner
import feature.qfeHelper.QFEHelper

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class AircraftListChild(val component: MainEventExecutor): Child()
        data class ChecklistsChild(val component: Checklists): Child()
        data class ChecklistDetailsChild(val component: ChecklistDetails): Child()
        data class FuelCalculatorChild(val component: FuelCalculator): Child()
        data class MetarScannerChild(val component: MetarScanner): Child()
        data class AirportsBaseChild(val component: AirportsBase): Child()
        data class QFEHelperChild(val component: QFEHelper): Child()
    }
}