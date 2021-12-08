package feature.metarscreen

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.reduce
import feature.metarscreen.model.MetarUi
import feature.metarscreen.model.parseMetar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.AirportService
import services.commonApi.Result
import services.commonApi.forError
import services.commonApi.forSuccess
import services.metarService.MetarService

class MetarScanner(
    val onBack: () -> Unit
) : KoinComponent {

    private val metarService: MetarService by inject()
    private val airportService: AirportService by inject()

    val state = MutableValue(WindViewState())

    private var metarJob: Job? = null
    private var airportJob: Job? = null

    fun onEvent(event: MetarUiEvent) = when (event) {
        is MetarUiEvent.SubmitAngle -> submitAngle(event.new)
        is MetarUiEvent.SubmitICAO -> submitICAO(event.station, event.scope)
        is MetarUiEvent.ShowInfoDialog -> showInfoDialog(true)
        is MetarUiEvent.DismissInfoDialog -> showInfoDialog(false)
    }

    private fun submitAngle(new: Int) {
        metarJob?.cancel().also { metarJob = null }
        airportJob?.cancel().also { airportJob = null }
        state.value = WindViewState(data = MetarUi(userAngle = new))
    }

    private fun setErrorState(error: Result.Error) {
        state.reduce {
            it.copy(
                isLoading = false,
                error = error.message
            )
        }
    }

    private suspend fun fetchMetar(station: String) {
        val response = metarService.getMetar(station)
        response.forSuccess { metarApi ->
            state.reduce {
                val metar = metarApi.parseMetar()
                it.copy(
                    data = MetarUi(
                        metarAngle = metar?.windDirection ?: it.data.metarAngle,
                        metarSpeedKt = metar?.windSpeedKt ?: it.data.metarSpeedKt,
                        airport = metarApi.name,
                        rawMetar = metarApi.metar,
                        rawTaf = metarApi.taf,
                    ),
                    isLoading = false,
                    error = if (metar == null) { "Metar has no correct wind information" } else null
                )
            }
        }
        response.forError { error -> setErrorState(error) }
    }

    private suspend fun fetchAirport(icao: String) {
        val response = airportService.getAirportByICAO(icao)
        response.forSuccess { airport ->
            state.reduce {
                it.copy(
                    airport = airport,
                    isLoading = false
                )
            }
        }
        response.forError { error -> setErrorState(error) }
    }

    private fun submitICAO(station: String, scope: CoroutineScope) {
        // Say to Ui that loading has started
        state.reduce { it.copy(isLoading = true) }
        // Start requests to METAR and Airport API in parallel
        metarJob = scope.launch { fetchMetar(station) }
        airportJob = scope.launch { fetchAirport(station) }
    }

    private fun showInfoDialog(show: Boolean = true) {
        state.reduce {
            it.copy(showInfo = show)
        }
    }
}