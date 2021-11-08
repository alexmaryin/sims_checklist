package decompose

import database.AircraftBaseImpl
import repository.AircraftRepository
import repository.AircraftRepositoryImpl

actual class SimViewState {
    actual val aircraftRepository: AircraftRepository = AircraftRepositoryImpl(
        database = AircraftBaseImpl()
    )
}