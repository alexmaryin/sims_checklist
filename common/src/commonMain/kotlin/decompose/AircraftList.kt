package decompose

import model.Aircraft

class AircraftList(
    val aircraftList: List<Aircraft>,
    val onSelected: (aircraftId: Int) -> Unit,
    val onCalculatorSelect: (aircraftId: Int) -> Unit
)
