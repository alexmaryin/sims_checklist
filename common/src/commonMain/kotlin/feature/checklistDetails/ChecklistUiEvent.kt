package feature.checklistDetails

sealed class ChecklistUiEvent {
    object Clear : ChecklistUiEvent()
    data class Toggle(val toggledIndex: Int) : ChecklistUiEvent()
    object Undo : ChecklistUiEvent()
    object Back : ChecklistUiEvent()
}
