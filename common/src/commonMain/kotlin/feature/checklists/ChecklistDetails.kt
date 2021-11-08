package feature.checklists

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.reduce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import feature.checklists.model.Checklist

class ChecklistDetails(
    checklist: Checklist,
    val onFinished: () -> Unit,
    private val updateBaseChecklist: (values: List<Boolean>) -> Unit
){
    val state = MutableValue(checklist)

    fun clear() {
        state.reduce {
            it.copy(items = state.value.items.map { item -> item.copy(checked = false) })
        }
        updateBaseChecklist(state.value.items.map { it.checked })
    }

    fun toggle(toggledIndex: Int) = CoroutineScope(Dispatchers.Default).launch {
        state.reduce {
            it.copy(items = state.value.items.mapIndexed { index, item ->
                item.copy(checked = if(index == toggledIndex) !item.checked else item.checked)
            })
        }
        updateBaseChecklist(state.value.items.map { it.checked })
    }
}

