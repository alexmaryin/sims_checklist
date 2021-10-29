package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import model.Aircraft
import ui.AircraftListScreen

class AircraftList(
    private val aircraftList: List<Aircraft>,
    val onSelected: (aircraft: Aircraft) -> Unit
) {
    val state: State<List<Aircraft>> get() = mutableStateOf(aircraftList)
}

@Composable
fun AircraftListUi(list: AircraftList) {
    AircraftListScreen(items = list.state.value, onAircraftClick = list.onSelected)
}