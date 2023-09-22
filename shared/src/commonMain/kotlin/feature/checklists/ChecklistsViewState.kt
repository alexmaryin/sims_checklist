package feature.checklists

import feature.checklistDetails.model.Checklist

class ChecklistsViewState(
    val caption: String,
    val list: List<Checklist>,
    val snackBar: ListSnackBarState? = null
)

sealed class ListSnackBarState(
    val message: String,
    val button: String,
    val event: ChecklistsUiEvent
) {
    data object Undo : ListSnackBarState(
        message = "Clear all checklists?",
        button = "Clear",
        event = ChecklistsUiEvent.ClearAll
    )
}
