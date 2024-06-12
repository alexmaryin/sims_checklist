package decompose

import kotlinx.serialization.Serializable

@Serializable
sealed interface Configuration {

    @Serializable
    data object AircraftList : Configuration

    @Serializable
    data class Checklists(val aircraftId: Int) : Configuration

    @Serializable
    data class Checklist(val aircraftId: Int, val checklistId: Int) : Configuration

    @Serializable
    data class FuelCalculator(val aircraftId: Int) : Configuration

    @Serializable
    data object MetarScanner : Configuration

    @Serializable
    data object AirportsBase : Configuration

    @Serializable
    data object QFEHelper : Configuration
}
