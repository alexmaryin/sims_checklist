package feature.metarscreen

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import feature.metarscreen.model.*
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

    data class Loading(var loadMetar: Boolean = false, var loadAirport: Boolean = false) {
        val state get() = loadMetar || loadAirport
    }

    private val metarService: MetarService by inject()
    private val airportService: AirportService by inject()

    val state = MutableValue(WindViewState())

    private var metarJob: Job? = null
    private var airportJob: Job? = null

    private val combineLoading = Loading()

    fun onEvent(event: MetarUiEvent) = when (event) {
        is MetarUiEvent.SubmitAngle -> submitAngle(event.new)
        is MetarUiEvent.SubmitICAO -> submitICAO(event.station, event.scope)
        is MetarUiEvent.SubmitRunway-> submitRunway(event.new)
        is MetarUiEvent.ShowInfoDialog -> showInfoDialog(true)
        is MetarUiEvent.DismissInfoDialog -> showInfoDialog(false)
    }

    private fun submitAngle(new: Int) {
        metarJob?.cancel().also { metarJob = null }
        airportJob?.cancel().also { airportJob = null }
        state.value = WindViewState(data = MetarUi(userAngle = new))
    }

    private fun submitRunway(new: RunwayUi) {
        state.update {
            if (state.value.data.metarAngle != null && state.value.data.metarSpeedKt != null) {
                it.copy(
                    runway = new.withCalculatedWind(
                        state.value.data.metarSpeedKt!!,
                        state.value.data.metarAngle!!
                    )
                )
            } else {
                it.copy(runway = new)
            }
        }
    }

    private fun setErrorState(error: Result.Error) {
        state.update {
            it.copy(
                isLoading = combineLoading.state,
                error = error.message
            )
        }
    }

    private suspend fun fetchMetar(station: String) {
        val response = metarService.getMetar(station)
        combineLoading.loadMetar = false
        response.forSuccess { metarApi ->
            state.update {
                val metar = metarApi.parseMetar()
                it.copy(
                    data = MetarUi(
                        metarAngle = metar.wind?.direction ?: it.data.metarAngle,
                        metarSpeedKt = metar.wind?.speedKt ?: it.data.metarSpeedKt,
                        airport = metarApi.name,
                        rawMetar = metarApi.metar,
                        rawTaf = metarApi.taf,
                    ),
                    isLoading = combineLoading.state,
                    error = if (metar.wind == null) { "METAR has incorrect wind information" } else null
                )
            }
        }
        response.forError { error -> setErrorState(error) }
    }

    private suspend fun fetchAirport(icao: String) {
        val response = airportService.getAirportByICAO(icao)
        combineLoading.loadAirport = false
        response.forSuccess { airport ->
            state.update {
                it.copy(
                    airport = airport,
                    isLoading = combineLoading.state,
                    runway = airport.runways.firstOrNull()?.toUi() ?: RunwayUi()
                )
            }
        }
        response.forError { error -> setErrorState(error) }
    }

    private fun submitICAO(station: String, scope: CoroutineScope) {
        // Say to Ui that loading has started
        combineLoading.loadMetar = true
        combineLoading.loadAirport = true
        state.update { it.copy(isLoading = combineLoading.state, airport = null) }
        // Start requests to METAR and Airport API in parallel
        metarJob = scope.launch { fetchMetar(station) }
        airportJob = scope.launch { fetchAirport(station) }
    }

    private fun showInfoDialog(show: Boolean = true) {
        state.update {
            it.copy(showInfo = show)
        }
    }
}