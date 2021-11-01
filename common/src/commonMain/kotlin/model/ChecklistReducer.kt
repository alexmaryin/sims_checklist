package model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChecklistReducer(initialState: Checklist) : ChecklistViewState {

    private val _state = MutableStateFlow(initialState)
    override val state get() = _state.asStateFlow()

    override fun toggleItem(index: Int) {
        val result = _state.tryEmit(_state.value.apply {
            items[index].toggle()
        })
        println("Toggle of item $index ${if(!result) "not" else ""} invoked emit of new state")
    }

    override fun clear() {
        val result = _state.tryEmit(_state.value.apply {
            clear()
        })
        println("Clear button ${if(!result) "not" else ""} invoked emit of new state")
    }
}