package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import model.Aircraft
import model.Checklist
import ui.ChecklistsScreen

class Checklists(
    aircraft: Aircraft,
    val onBack: () -> Unit,
    val onSelected: (checklist: Checklist) -> Unit
) {
    private val _state = MutableValue(aircraft.checklists)
    val state: Value<List<Checklist>> = _state
    val name = aircraft.name

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
