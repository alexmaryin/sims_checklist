package viewState

import model.AircraftBase
import model.AircraftBaseImpl

actual class SimViewState {
    actual val aircraftBase: AircraftBase = AircraftBaseImpl()
}