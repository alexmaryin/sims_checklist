package feature.checklistDetails

import com.arkivanov.decompose.value.MutableValue
import repository.AircraftRepository

class ChecklistDetails(
    private val aircraftId: Int,
    private val checklistId: Int,
    private val repository: AircraftRepository,
    private val onFinished: () -> Unit,
) {
    val state = MutableValue(ChecklistViewState(repository.getChecklist(aircraftId, checklistId)))

    fun onEvent(event: ChecklistUiEvent) {
        var showUndo = false

        when (event) {
            is ChecklistUiEvent.Clear -> {
                repository.clearChecklist(aircraftId, checklistId)
                showUndo = true
            }
            is ChecklistUiEvent.Toggle -> {
                repository.toggleChecklistItem(aircraftId, checklistId, event.toggledIndex)
            }
            is ChecklistUiEvent.Undo -> {
                repository.undoChecklistChanges(aircraftId, checklistId)
            }
            is ChecklistUiEvent.Back -> onFinished()
        }

        state.value = ChecklistViewState(
            checklist = repository.getChecklist(aircraftId, checklistId),
            snackBar = when {
                showUndo -> SnackBarState.Undo(state.value.checklist.caption)
                state.value.checklist.isCompleted && event != ChecklistUiEvent.Back ->
                    SnackBarState.Back(state.value.checklist.caption)
                else -> null
            }
        )
    }
}

