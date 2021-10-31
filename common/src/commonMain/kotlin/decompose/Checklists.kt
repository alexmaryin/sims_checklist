package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import model.Aircraft
import model.Checklist
import ui.ChecklistsScreen

class Checklists(
    componentContext: ComponentContext,
    aircraft: Aircraft,
    val onBack: () -> Unit,
    val onSelected: (checklist: Checklist) -> Unit
) : ComponentContext by componentContext {

    private class SavedState(aircraft: Aircraft) : InstanceKeeper.Instance {

        val state = MutableValue(aircraft.checklists)
        val name = aircraft.name

        override fun onDestroy() {}
    }

    private val instance = instanceKeeper.getOrCreate { SavedState(aircraft) }
    val state: Value<List<Checklist>> = instance.state
    val name = instance.name
}

@Composable
fun ChecklistsUi(list: Checklists) {

    val state by list.state.subscribeAsState()

    ChecklistsScreen(
        name = list.name,
        items = state,
        onBackClick = list.onBack,
        onChecklistClick = list.onSelected
    )
}
