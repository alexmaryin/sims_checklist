package decompose

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import model.Checklist
import model.Item

class ChecklistDetails(
    checklist: Checklist,
    private val onFinished: () -> Unit,
    private val updateBaseChecklist: (items: List<Item>) -> Unit
){
    val caption = checklist.caption

    private val _items = MutableStateFlow(checklist.items)
    val items get() = _items.asStateFlow()

    fun clear() {
        val new = items.value.map{ item ->
            item.copy(checked = false)
        }
        _items.tryEmit(new)
    }

    fun toggle(toggledIndex: Int) {
        val new = items.value.mapIndexed { index, item ->
            item.copy(checked = if(index == toggledIndex) !item.checked else item.checked)
        }
        _items.tryEmit(new)
    }
    fun close() {
        updateBaseChecklist(items.value)
        onFinished()
    }
}

