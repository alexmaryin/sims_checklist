package model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChecklistViewState {
    fun state(): StateFlow<Checklist>
    fun toggleItem(index: Int)
    fun clear()
    val debugLog: Flow<String>
}