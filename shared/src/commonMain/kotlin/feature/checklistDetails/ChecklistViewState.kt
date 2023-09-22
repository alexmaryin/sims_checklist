package feature.checklistDetails

import feature.checklistDetails.model.Checklist

class ChecklistViewState(
    val checklist: Checklist,
    val snackBar: SnackBarState? = null
)

sealed class SnackBarState(
    val message: String,
    val button: String,
    val event: ChecklistUiEvent,
) {
    data class Undo(val caption: String) : SnackBarState(
        message = "$caption checklist is cleared",
        button = "Undo?",
        event = ChecklistUiEvent.Undo,
    )

    data class Back(val caption: String) : SnackBarState(
        message = "$caption checklist is completed",
        button = "Close?",
        event = ChecklistUiEvent.Back,
    )
}
