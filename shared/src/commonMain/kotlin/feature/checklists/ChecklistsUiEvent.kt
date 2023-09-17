package feature.checklists

sealed class ChecklistsUiEvent {
    data class SelectChecklist(val checklistId: Int) : ChecklistsUiEvent()
    object ConfirmClear : ChecklistsUiEvent()
    object ClearAll : ChecklistsUiEvent()
    object Back : ChecklistsUiEvent()
}