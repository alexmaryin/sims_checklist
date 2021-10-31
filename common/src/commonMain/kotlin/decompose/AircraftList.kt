package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import model.Aircraft
import ui.AircraftListScreen

class AircraftList(
    val aircraftList: List<Aircraft>,
    val onSelected: (aircraft: Aircraft) -> Unit
) {

    private val _state = MutableValue(aircraftList)
    val state: Value<List<Aircraft>> = _state
}

@Composable
fun AircraftListUi(list: AircraftList) {
    val state by list.state.subscribeAsState()
    AircraftListScreen(items = state, onAircraftClick = list.onSelected)
}
