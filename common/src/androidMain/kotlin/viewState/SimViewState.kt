package viewState

import androidx.lifecycle.ViewModel
import model.AircraftBase
import model.AircraftBaseImpl

actual class SimViewState : ViewModel() {
    actual val aircraftBase: AircraftBase = AircraftBaseImpl()
}