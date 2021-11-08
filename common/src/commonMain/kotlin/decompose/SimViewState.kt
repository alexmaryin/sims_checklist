package decompose

import repository.AircraftRepository

expect class SimViewState {
    val aircraftRepository: AircraftRepository
}