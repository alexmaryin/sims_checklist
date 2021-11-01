package decompose

import androidx.compose.runtime.Composable
import model.Aircraft
import ui.AircraftListScreen

class AircraftList(
    val aircraftList: List<Aircraft>,
    val onSelected: (aircraft: Aircraft) -> Unit
)

@Composable
fun AircraftListUi(list: AircraftList) {
    AircraftListScreen(items = list.aircraftList, onAircraftClick = list.onSelected)
}
