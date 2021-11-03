package decompose

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import model.Aircraft
import model.Checklist

class Checklists(
    aircraft: Aircraft,
    val onBack: () -> Unit,
    val onSelected: (checklist: Checklist) -> Unit,
    private val clearBaseChecklists: () -> Unit,
) {
    private val _state = MutableStateFlow(ComponentData(aircraft.checklists, aircraft.name))
    val state get() = _state.asStateFlow()

    fun clear() {
        clearBaseChecklists()
        val new = ComponentData(state.value.checklists.map { checklist ->
            checklist.copy(items = checklist.items.map { it.copy(checked = false) })
        }, state.value.name)
        _state.tryEmit(new)

    }

    class ComponentData(
        val checklists: List<Checklist>,
        val name: String
    )
}