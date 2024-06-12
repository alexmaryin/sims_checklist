package decompose

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.serialization.Serializable

sealed class Configuration : Parcelable {

    @Serializable
    data object AircraftList : Configuration() {
        private fun readResolve(): Any = AircraftList
    }

    @Serializable
    data class Checklists(val aircraftId: Int) : Configuration()

    @Serializable
    data class Checklist(val aircraftId: Int, val checklistId: Int) : Configuration()

    @Serializable
    data class FuelCalculator(val aircraftId: Int) : Configuration()

    @Serializable
    data object MetarScanner : Configuration() {
        private fun readResolve(): Any = MetarScanner
    }

    @Serializable
    data object AirportsBase : Configuration() {
        private fun readResolve(): Any = AirportsBase
    }

    @Serializable
    data object QFEHelper : Configuration() {
        private fun readResolve(): Any = QFEHelper
    }
}
