package decompose

import model.Aircraft

class AircraftList(
    val aircraftList: List<Aircraft>,
    val onSelected: (aircraft: Aircraft) -> Unit
)

