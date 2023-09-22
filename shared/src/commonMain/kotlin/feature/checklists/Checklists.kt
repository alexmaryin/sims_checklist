package feature.checklists

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import repository.AircraftRepository

class Checklists(
    private val aircraftId: Int,
    private val repository: AircraftRepository,
    private val onBack: () -> Unit,
    private val onSelected: (checklistId: Int) -> Unit,
) {
    val state = MutableValue(ChecklistsViewState(
        caption = repository.getById(aircraftId).name,
        list = repository.getById(aircraftId).checklists
    ))

    fun onEvent(event: ChecklistsUiEvent) {
        var snackBar: ListSnackBarState? = null
        when (event) {
            is ChecklistsUiEvent.ConfirmClear -> snackBar = ListSnackBarState.Undo
            is ChecklistsUiEvent.ClearAll -> repository.clearBaseChecklists(aircraftId)
            is ChecklistsUiEvent.Back -> onBack()
            is ChecklistsUiEvent.SelectChecklist -> onSelected(event.checklistId)
        }
        state.update {
            ChecklistsViewState(
                caption = it.caption,
                list = repository.getById(aircraftId).checklists,
                snackBar = snackBar
            )
        }
    }
}