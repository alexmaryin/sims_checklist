package decompose

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Configuration : Parcelable {

    @Parcelize
    data object AircraftList : Configuration() {
        private fun readResolve(): Any = AircraftList
    }

    @Parcelize
    data class Checklists(val aircraftId: Int) : Configuration()

    @Parcelize
    data class Checklist(val aircraftId: Int, val checklistId: Int) : Configuration()

    @Parcelize
    data class FuelCalculator(val aircraftId: Int) : Configuration()

    @Parcelize
    data object MetarScanner : Configuration() {
        private fun readResolve(): Any = MetarScanner
    }

    @Parcelize
    data object AirportsBase : Configuration() {
        private fun readResolve(): Any = AirportsBase
    }

    @Parcelize
    data object QFEHelper : Configuration() {
        private fun readResolve(): Any = QFEHelper
    }
}
