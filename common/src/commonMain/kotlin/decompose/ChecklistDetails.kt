package decompose

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import model.Checklist
import model.Item

class ChecklistDetails(
    checklist: Checklist,
    val onFinished: () -> Unit,
    private val updateBaseChecklist: (items: List<Item>) -> Unit
){
    private val _state = MutableStateFlow(ComponentData(checklist.items, checklist.caption))
    val state get() = _state.asStateFlow()

    fun clear() {
        val new = ComponentData(
            state.value.items.map { item -> item.copy(checked = false) },
            state.value.caption
        )
        _state.tryEmit(new)
        updateBaseChecklist(state.value.items)
    }

    fun toggle(toggledIndex: Int) {
        val new = ComponentData(
            state.value.items.mapIndexed { index, item ->
                item.copy(checked = if(index == toggledIndex) !item.checked else item.checked)
            },
            state.value.caption
        )
        _state.tryEmit(new)
        updateBaseChecklist(state.value.items)
    }

    class ComponentData(
        val items: List<Item>,
        val caption: String
    )
}

