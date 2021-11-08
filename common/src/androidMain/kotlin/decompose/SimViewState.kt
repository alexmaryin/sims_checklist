package decompose

import androidx.lifecycle.ViewModel
import database.AircraftBaseImpl
import repository.AircraftRepository
import repository.AircraftRepositoryImpl

actual class SimViewState : ViewModel() {
    actual val aircraftRepository: AircraftRepository = AircraftRepositoryImpl(
        database = AircraftBaseImpl()
    )
}