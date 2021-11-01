package model

import kotlinx.coroutines.flow.StateFlow

interface ChecklistViewState {
    val state: StateFlow<Checklist>
    fun toggleItem(index: Int)
    fun clear()
}