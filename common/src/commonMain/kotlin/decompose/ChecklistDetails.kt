package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import model.Checklist
import ui.ChecklistScreen

class ChecklistDetails(
    checklist: Checklist,
    val onFinished: () -> Unit
) {
    private val _state = mutableStateOf(checklist)
    val state: State<Checklist> get() = _state
}

@Composable
fun ChecklistUi(items: ChecklistDetails) {
    ChecklistScreen(
        checklist = items.state.value,
        onBackClick = items.onFinished
    )
}
