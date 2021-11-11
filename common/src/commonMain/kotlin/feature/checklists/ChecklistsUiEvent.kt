package feature.checklists

sealed class ChecklistsUiEvent {
    object ConfirmClear : ChecklistsUiEvent()
    object ClearAll : ChecklistsUiEvent()
    object Back : ChecklistsUiEvent()
    data class SelectChecklist(val checklistId: Int) : ChecklistsUiEvent()
}