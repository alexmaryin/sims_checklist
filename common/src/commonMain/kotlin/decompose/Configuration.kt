package decompose

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Configuration : Parcelable {

    @Parcelize
    object AircraftList : Configuration()

    @Parcelize
    data class Checklists(val aircraftId: Int) : Configuration()

    @Parcelize
    data class Checklist(val aircraftId: Int, val checklistId: Int) : Configuration()

    @Parcelize
    data class FuelCalculator(val aircraftId: Int) : Configuration()

    @Parcelize
    object MetarScanner : Configuration()
}
