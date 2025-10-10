package feature.mainScreen

import feature.checklists.model.Aircraft

data class MainScreenViewState(
    val aircraftList: List<Aircraft> = emptyList(),
    val isLoading: Boolean = false,
    val updateMessage: String? = null,
    val snack: MainScreenSnack? = null
)

sealed class MainScreenSnack(val message: String, val event: MainScreenEvent, val label: String) {
    data class Close(val msg: String) : MainScreenSnack(
        msg,
        MainScreenEvent.ClearSnack,
        "Close"
    )

    data class StartUpdate(val msg: String) : MainScreenSnack(
        msg,
        MainScreenEvent.StartUpdate,
        "Update"
    )
}
