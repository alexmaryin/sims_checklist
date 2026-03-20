package feature.coldTemperature

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import feature.coldTemperature.ui.model.ColdTemperatureWaypoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.AirportService
import services.coldTemperature.ColdTemperatureWaypointStore
import services.coldTemperature.StoredColdTemperatureWaypoint
import services.commonApi.Result

class ColdTemperatureCorrector(
    private val componentContext: ComponentContext,
    private val icao: String? = null,
    val onBack: () -> Unit
) : KoinComponent, ComponentContext by componentContext {

    private val airportService: AirportService by inject()
    private val waypointStore: ColdTemperatureWaypointStore by inject()
    private val scope = componentContext.coroutineScope() + SupervisorJob()
    private var observeWaypointsJob: Job? = null

    val state = MutableValue(ColdTemperatureState())

    init {
        lifecycle.doOnStart {
            icao?.let(::submitICAO)
        }
    }

    fun onEvent(event: ColdTemperatureEvent) = when (event) {
        is ColdTemperatureEvent.SubmitICAO -> submitICAO(event.icao)
        is ColdTemperatureEvent.SubmitTemperature -> submitTemperature(event.celsius)
        is ColdTemperatureEvent.SubmitAirportElevation -> submitAirportElevation(event.feet)
        is ColdTemperatureEvent.AddWaypoint -> addWaypoint(event.name, event.altitudeFeet)
        is ColdTemperatureEvent.DeleteWaypoint -> deleteWaypoint(event.name)
        ColdTemperatureEvent.ClearError -> clearError()
    }

    private fun submitICAO(icao: String) {
        val normalizedIcao = icao.trim().uppercase()
        if (normalizedIcao.isBlank()) return

        state.update {
            it.copy(
                isLoading = true,
                airportICAO = normalizedIcao,
                error = null,
                waypoints = emptyList()
            )
        }

        scope.launch {
            when (val response = airportService.getAirportByICAO(normalizedIcao)) {
                is Result.Success -> {
                    val airport = response.value
                    state.update {
                        it.copy(
                            airportICAO = airport.icao,
                            airportName = airport.name,
                            airportElevationFeet = airport.elevation,
                            isLoading = false,
                            error = null
                        )
                    }
                    observeWaypoints(airport.icao)
                }

                is Result.Error -> {
                    state.update { it.copy(isLoading = false, error = response.message) }
                }
            }
        }
    }

    private fun submitTemperature(celsius: Int) {
        state.update {
            it.copy(
                temperatureCelsius = celsius,
                waypoints = it.waypoints.recalculate(
                    airportElevation = it.airportElevationFeet,
                    temperatureCelsius = celsius
                )
            )
        }
    }

    private fun submitAirportElevation(feet: Int) {
        observeWaypointsJob?.cancel()
        state.update {
            it.copy(
                airportElevationFeet = feet,
                airportICAO = null,
                airportName = null,
                waypoints = it.waypoints.recalculate(
                    airportElevation = feet,
                    temperatureCelsius = it.temperatureCelsius
                )
            )
        }
    }

    private fun addWaypoint(name: String, altitudeFeet: Int) {
        val upperName = name.trim().uppercase()
        if (upperName.isBlank()) {
            state.update { it.copy(error = "Waypoint name should not be empty") }
            return
        }

        if (upperName in state.value.waypoints.map { it.name }) {
            state.update { it.copy(error = "Name $upperName already exists") }
            return
        }

        val currentIcao = state.value.airportICAO
        state.update { it.copy(error = null) }
        if (currentIcao.isNullOrBlank()) return

        scope.launch {
            waypointStore.addWaypoint(
                icao = currentIcao,
                name = name.trim(),
                altitudeFeet = altitudeFeet
            )
        }
    }

    private fun deleteWaypoint(name: String) {
        val currentIcao = state.value.airportICAO ?: return
        scope.launch {
            waypointStore.deleteWaypoint(currentIcao, name)
        }
    }

    private fun clearError() {
        state.update { it.copy(error = null) }
    }

    private fun observeWaypoints(icao: String) {
        observeWaypointsJob?.cancel()
        observeWaypointsJob = scope.launch {
            waypointStore.observeWaypointsByIcao(icao).collectLatest { storedWaypoints ->
                state.update {
                    it.copy(
                        waypoints = storedWaypoints.toUiWaypoints().recalculate(
                            airportElevation = it.airportElevationFeet,
                            temperatureCelsius = it.temperatureCelsius
                        )
                    )
                }
            }
        }
    }
}

private fun List<StoredColdTemperatureWaypoint>.toUiWaypoints(): List<ColdTemperatureWaypoint> =
    map { waypoint ->
        ColdTemperatureWaypoint(
            name = waypoint.name,
            altitudeFeet = waypoint.altitudeFeet
        )
    }
