package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import model.Checklist
import ui.ChecklistScreen

class ChecklistDetails(
    checklist: Checklist,
    val onFinished: () -> Unit
) {
    private val _state = MutableValue(checklist)
    val state: Value<Checklist> = _state
}

@Composable
fun ChecklistUi(items: ChecklistDetails) {
    val state by items.state.subscribeAsState()
    ChecklistScreen(
        checklist = state,
        onBackClick = items.onFinished
    )
}
