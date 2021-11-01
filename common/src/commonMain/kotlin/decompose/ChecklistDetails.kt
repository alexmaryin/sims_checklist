package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import model.Checklist
import model.ChecklistReducer
import ui.ChecklistScreen

class ChecklistDetails(
    componentContext: ComponentContext,
    checklist: Checklist,
    val onFinished: () -> Unit
) : ComponentContext by componentContext {

    private class SavedState(checklist: Checklist) : InstanceKeeper.Instance {

        val state = MutableValue(checklist)

        override fun onDestroy() {}
    }

    private val instance = instanceKeeper.getOrCreate { SavedState(checklist) }
    val state: Value<Checklist> = instance.state
}

@Composable
fun ChecklistUi(items: ChecklistDetails) {
    val state by items.state.subscribeAsState()
    ChecklistScreen(
        checklist = ChecklistReducer(state),
        onBackClick = items.onFinished
    )
}
