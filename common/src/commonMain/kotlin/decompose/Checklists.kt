package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import model.Aircraft
import model.Checklist
import ui.ChecklistsScreen

class Checklists(
    aircraft: Aircraft,
    val onSelected: (checklist: Checklist) -> Unit
) {
    private val _state = mutableStateOf(aircraft.checklists)
    val state: State<List<Checklist>> get() = _state
}

@Composable
fun ChecklistsUi(list: Checklists) {
    ChecklistsScreen(
        items = list.state.value,
        onChecklistClick = list.onSelected
    )
}
