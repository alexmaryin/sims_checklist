package feature.metarscreen

import alexmaryin.metarkt.models.Wind
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import commonUi.saveableMutableValue
import feature.metarscreen.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.AirportService
import services.airportService.model.dropClosedRunways
import services.atisService.AtisService
import services.commonApi.ErrorType
import services.commonApi.Result
import services.commonApi.forError
import services.commonApi.forSuccess
import services.metarService.MetarService

class MetarScanner(
    val componentContext: ComponentContext,
    val icao: String? = null,
    val onOpenQfeHelper: (icao: String, qfe: Int?, celsius: Int?) -> Unit,
    val onOpenColdTemperature: (icao: String, temperature: Int?) -> Unit,
    val onBack: () -> Unit
) : KoinComponent, ComponentContext by componentContext {

    init {
        lifecycle.doOnStart {
            fetchHistoryAirports()
            icao?.let { submitICAO(it) }
        }
    }

    data class Loading(var loadMetar: Boolean = false, var loadTaf: Boolean = false, var loadAirport: Boolean = false, var loadAtis: Boolean = false) {
        val state get() = loadMetar || loadTaf || loadAirport || loadAtis
    }

    private val metarService: MetarService by inject()
    private val airportService: AirportService by inject()
    private val atisService: AtisService by inject()

    val state by saveableMutableValue(MetarScreenViewState.serializer(), init = ::MetarScreenViewState)

    private var metarJob: Job? = null
    private var tafJob: Job? = null
    private var airportJob: Job? = null
    private var atisJob: Job? = null

    private val scope = componentContext.coroutineScope() + SupervisorJob()

    private val combineLoading = Loading()

    fun onEvent(event: MetarUiEvent) = when (event) {
        is MetarUiEvent.SubmitWindAngle -> submitWindAngle(event.new)
        is MetarUiEvent.SubmitICAO -> submitICAO(event.station)
        is MetarUiEvent.SubmitRunway -> submitRunway(event.new)
        is MetarUiEvent.ShowInfoDialog -> showInfoDialog(true)
        is MetarUiEvent.DismissInfoDialog -> showInfoDialog(false)
        is MetarUiEvent.SubmitRunwayAngle -> submitRunwayAngle(event.new)
        is MetarUiEvent.SubmitWindSpeed -> submitWindSpeed(event.new)
        is MetarUiEvent.SelectInfoTab -> selectInfoTab(event.tab)
        is MetarUiEvent.LoadTopLatest -> fetchHistoryAirports()
        is MetarUiEvent.OpenQfeHelper -> openQfeHelper()
        is MetarUiEvent.OpenColdTemperature -> openColdTemperature()
    }

    private fun MetarScreenViewState.updateRunwayWind(new: RunwayUi = state.value.runway): MetarScreenViewState = copy(
        runway = new.withCalculatedWind(
            state.value.metar?.toWind()
                ?: Wind(state.value.data.userAngle, speed = state.value.data.userSpeed)
        )
    )

    private fun submitWindAngle(new: Int) {
        metarJob?.cancel().also { metarJob = null }
        tafJob?.cancel().also { tafJob = null }
        airportJob?.cancel().also { airportJob = null }
        atisJob?.cancel().also { atisJob = null }
        state.update {
            it.copy(data = state.value.data.copy(userAngle = new, metarAngle = null, metarSpeedKt = null))
                .updateRunwayWind(state.value.runway)
        }
    }

    private fun submitWindSpeed(new: Int) {
        metarJob?.cancel().also { metarJob = null }
        tafJob?.cancel().also { tafJob = null }
        airportJob?.cancel().also { airportJob = null }
        atisJob?.cancel().also { atisJob = null }
        state.update {
            it.copy(data = state.value.data.copy(userSpeed = new, metarAngle = null, metarSpeedKt = null))
                .updateRunwayWind(state.value.runway)
        }
    }

    private fun submitRunwayAngle(new: Int) {
        val runwayUi = new.toRunwayUi()
        state.update { it.copy(airport = null, metar = null) }
        submitRunway(runwayUi)
    }

    private fun submitRunway(new: RunwayUi) {
        state.update { it.updateRunwayWind(new) }
    }

    private fun setErrorState(error: Result.Error) {
        state.update {
            it.copy(
                isLoading = combineLoading.state,
                error = error.message,
                metar = null,
                data = MetarUi()
            )
        }
    }

    private suspend fun fetchMetar(station: String) {
        val response = metarService.getMetar(station)
        combineLoading.loadMetar = false
        response.forSuccess { metarRaw ->
            state.update {
                val metar = parseMetar(metarRaw)
                it.copy(
                    metar = metar.toMetarData(),
                    data = it.data.copy(
                        metarAngle = metar.wind?.direction ?: it.data.metarAngle,
                        metarSpeedKt = metar.wind?.speedKt ?: it.data.metarSpeedKt,
                        rawMetar = metarRaw,
                    ),
                    isLoading = combineLoading.state,
                    error = if (metar.wind == null) {
                        "METAR has no correct wind information"
                    } else null
                )
            }
        }
        response.forError { error -> setErrorState(error) }
    }

    private suspend fun fetchTaf(station: String) {
        val response = metarService.getTaf(station)
        combineLoading.loadTaf = false
        response.forSuccess { tafRaw ->
            state.update {
                it.copy(
                    data = it.data.copy(rawTaf = tafRaw),
                    isLoading = combineLoading.state
                )
            }
        }
    }

    private suspend fun fetchAirport(icao: String) {
        val response = airportService.getAirportByICAO(icao)
        combineLoading.loadAirport = false
        response.forSuccess { airport ->
            state.update {
                it.copy(
                    airport = airport.dropClosedRunways(),
                    isLoading = combineLoading.state,
                    runway = airport.runways.firstOrNull()?.toUi() ?: RunwayUi()
                )
            }
        }
        response.forError { error -> setErrorState(error) }
    }

    private suspend fun fetchAtis(station: String) {
        val response = atisService.getDatis(station)
        combineLoading.loadAtis = false
        response.forSuccess { entries ->
            state.update {
                it.copy(
                    datis = DatisUi(entries = entries),
                    isLoadingAtis = false,
                    isLoading = combineLoading.state
                )
            }
        }
        response.forError { error ->
            if (error.type == ErrorType.EMPTY_RESULT) {
                state.update {
                    it.copy(
                        datis = null,
                        isLoadingAtis = false,
                        isLoading = combineLoading.state
                    )
                }
            } else {
                state.update {
                    it.copy(
                        datis = DatisUi(error = "Failed to load D-ATIS: ${error.message}"),
                        isLoadingAtis = false,
                        isLoading = combineLoading.state
                    )
                }
            }
        }
    }

    private fun fetchHistoryAirports() {
        scope.launch {
            val response = airportService.getAirportsHistory()
            response.forSuccess { airports ->
                state.update {
                    it.copy(
                        historyAirports = airports
                    )
                }
            }
        }
    }

    private fun submitICAO(station: String) {
        combineLoading.loadMetar = true
        combineLoading.loadTaf = true
        combineLoading.loadAirport = true
        combineLoading.loadAtis = true
        metarJob?.cancel()
        tafJob?.cancel()
        airportJob?.cancel()
        atisJob?.cancel()
        state.update { it.copy(isLoading = combineLoading.state, airport = null, historyAirports = emptyList(), selectedInfoTab = 0, isLoadingAtis = true, data = it.data.copy(lastIcao = station)) }
        metarJob = scope.launch { fetchMetar(station) }
        tafJob = scope.launch { fetchTaf(station) }
        airportJob = scope.launch { fetchAirport(station) }
        atisJob = scope.launch { fetchAtis(station) }
        fetchHistoryAirports()
    }

    private fun selectInfoTab(tab: Int) {
        state.update { it.copy(selectedInfoTab = tab) }
    }

    private fun showInfoDialog(show: Boolean = true) {
        state.update {
            it.copy(showInfo = show)
        }
    }

    private fun openQfeHelper() {
        state.value.airport?.let {
            onOpenQfeHelper(
                it.icao,
                state.value.metar?.pressureQFEmmHg,
                state.value.metar?.temperature
            )
        }
    }

    private fun openColdTemperature() {
        state.value.airport?.let { airport ->
            state.value.metar?.temperature?.let { temperature ->
                onOpenColdTemperature(
                    airport.icao,
                    temperature
                )
            }
        }
    }
}