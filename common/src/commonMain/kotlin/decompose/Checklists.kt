package decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.reduce
import model.Aircraft
import model.Checklist

class Checklists(
    aircraft: Aircraft,
    val onBack: () -> Unit,
    val onSelected: (checklist: Checklist) -> Unit,
    private val clearBaseChecklists: () -> Unit,
) {
    val state = MutableValue(aircraft)

    fun clear() {
        state.reduce {
            it.copy(
                checklists = it.checklists.map { checklist ->
                    checklist.copy(items = checklist.items.map { item ->
                        item.copy(checked = false) })
                }
            )
        }
        clearBaseChecklists()
    }
}