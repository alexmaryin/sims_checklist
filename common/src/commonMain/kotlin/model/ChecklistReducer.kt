package model

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChecklistReducer(initialState: Checklist) : ChecklistViewState {

    private val _state = MutableStateFlow(initialState)
    override fun state() = _state.asStateFlow()

    private val debug = MutableStateFlow(0)
    override val debugLog: Flow<String> get() = debug.transform { emit("emit $it") }

    override fun toggleItem(index: Int) {
        MainScope().launch {
            val new = _state.value.apply { items[index].toggle() }
            _state.emit(new)
            debug.value += 1
        }
    }

    override fun clear() {
        val result = _state.tryEmit(_state.value.apply {
            clear()
        })
        println("Clear button ${if(!result) "not" else ""} invoked emit of new state")
    }
}