package feature.checklists

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AircraftRepository

class Checklists(
    private val aircraftId: Int,
    private val onBack: () -> Unit,
    private val onSelected: (checklistId: Int) -> Unit,
) : KoinComponent {

    private val repository: AircraftRepository by inject()

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
            is ChecklistsUiEvent.Refresh -> Unit // just triggering for state update below
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