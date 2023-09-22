package feature.checklistDetails

sealed class ChecklistUiEvent {
    data class Toggle(val toggledIndex: Int) : ChecklistUiEvent()
    data object Clear : ChecklistUiEvent()
    data object Undo : ChecklistUiEvent()
    data object Back : ChecklistUiEvent()
}
