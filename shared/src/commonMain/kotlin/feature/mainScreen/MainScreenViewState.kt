package feature.mainScreen

import feature.checklists.model.Aircraft

data class MainScreenViewState(
    val aircraftList: List<Aircraft> = emptyList(),
    val isLoading: Boolean = false
)
