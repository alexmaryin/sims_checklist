package feature.checklistDetails

sealed class ChecklistUiEvent {
    data class Toggle(val toggledIndex: Int) : ChecklistUiEvent()
    object Clear : ChecklistUiEvent()
    object Undo : ChecklistUiEvent()
    object Back : ChecklistUiEvent()
}
