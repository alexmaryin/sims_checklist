package feature.checklistDetails

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import repository.AircraftRepository

class ChecklistDetails(
    private val aircraftId: Int,
    private val checklistId: Int,
    private val repository: AircraftRepository,
    private val onFinished: () -> Unit,
) {
    val state = with(repository.getChecklist(aircraftId, checklistId)) {
        MutableValue(ChecklistViewState(
            checklistId = id,
            caption = caption,
            items = items
        ))
    }

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

        state.update {
            val checklist = repository.getChecklist(aircraftId, checklistId)
            it.copy(
                items = checklist.items,
                isCompleted = checklist.isCompleted,
                snackBar = when {
                    showUndo -> SnackBarState.Undo(state.value.caption)
                    checklist.isCompleted && event != ChecklistUiEvent.Back ->
                        SnackBarState.Back(state.value.caption)
                    else -> null
                }
            )
        }
        println("CHECKLIST: ${state.value.items}")
    }
}

