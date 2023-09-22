package feature.checklists

sealed class ChecklistsUiEvent {
    data class SelectChecklist(val checklistId: Int) : ChecklistsUiEvent()
    data object ConfirmClear : ChecklistsUiEvent()
    data object ClearAll : ChecklistsUiEvent()
    data object Back : ChecklistsUiEvent()
}