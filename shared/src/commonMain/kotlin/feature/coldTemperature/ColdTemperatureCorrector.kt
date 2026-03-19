package feature.coldTemperature

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import commonUi.saveableMutableValue
import feature.coldTemperature.ui.model.ColdTemperatureWaypoint
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.AirportService
import services.commonApi.forError
import services.commonApi.forSuccess

class ColdTemperatureCorrector(
    private val componentContext: ComponentContext,
    private val icao: String? = null,
    val onBack: () -> Unit
) : KoinComponent, ComponentContext by componentContext {

    private val airportService: AirportService by inject()
    private val scope = componentContext.coroutineScope() + SupervisorJob()

    val state by saveableMutableValue(ColdTemperatureState.serializer(), init = ::ColdTemperatureState)

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
                error = null
            )
        }

        scope.launch {
            val response = airportService.getAirportByICAO(normalizedIcao)
            response.forSuccess { airport ->
                state.update {
                    it.copy(
                        airportICAO = airport.icao,
                        airportName = airport.name,
                        airportElevationFeet = airport.elevation,
                        isLoading = false,
                        error = null,
                        waypoints = emptyList()
                    )
                }
            }
            response.forError { error ->
                state.update { it.copy(isLoading = false, error = error.message) }
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

        state.update {
            val updated = it.waypoints + ColdTemperatureWaypoint(
                name = name.trim(),
                altitudeFeet = altitudeFeet
            )
            it.copy(
                error = null,
                waypoints = updated.recalculate(
                    airportElevation = it.airportElevationFeet,
                    temperatureCelsius = it.temperatureCelsius
                )
            )
        }
    }

    private fun deleteWaypoint(name: String) {
        state.update {
            it.copy(waypoints = it.waypoints.filterNot { waypoint -> waypoint.name == name })
        }
    }

    private fun clearError() {
        state.update { it.copy(error = null) }
    }
}
