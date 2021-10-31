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
import ui.AircraftListScreen

class AircraftList(
    componentContext: ComponentContext,
    aircraftList: List<Aircraft>,
    val onSelected: (aircraft: Aircraft) -> Unit
) : ComponentContext by componentContext {

    private class SavedState(aircraftList: List<Aircraft>) : InstanceKeeper.Instance {

        val state = MutableValue(aircraftList)

        override fun onDestroy() {}
    }

    private val instance = instanceKeeper.getOrCreate { SavedState(aircraftList) }
    val state: Value<List<Aircraft>> = instance.state
}

@Composable
fun AircraftListUi(list: AircraftList) {
    val state by list.state.subscribeAsState()
    AircraftListScreen(items = state, onAircraftClick = list.onSelected)
}
