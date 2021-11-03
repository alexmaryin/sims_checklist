package decompose

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import model.Aircraft
import model.AircraftBase
import model.Checklist
import kotlin.coroutines.EmptyCoroutineContext.get

class Checklists(
    aircraft: Aircraft,
    val onBack: () -> Unit,
    val onSelected: (checklist: Checklist) -> Unit,
    private val clearBaseChecklists: () -> Unit,
) {
    private val _state = MutableStateFlow(ComponentData(aircraft.checklists))
    val state get() = _state.asStateFlow()
    val name = aircraft.name

    fun clear() {
        clearBaseChecklists()
        val new = ComponentData(_state.value.checklists.map { checklist ->
            checklist.copy(items = checklist.items.map { it.copy(checked = false) })
        })
        _state.tryEmit(new)

    }

    class ComponentData(
        val checklists: List<Checklist>
    )
}