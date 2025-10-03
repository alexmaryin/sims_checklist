package feature.airportsBase

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.AirportService
import services.airportService.LocalBaseConverter
import services.airportService.updateService.AirportUpdateService
import services.commonApi.forError
import services.commonApi.forSuccess
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

class AirportsBase(
    private val componentContext: ComponentContext,
    private val onBack: () -> Unit
) : KoinComponent, AirportEventExecutor, ComponentContext by componentContext {

    private val updateService: AirportUpdateService by inject()
    private val csvConverter: LocalBaseConverter by inject()
    private val airportService: AirportService by inject()

    override val state = MutableValue(AirportsBaseViewState())

    private val scope = componentContext.coroutineScope() + SupervisorJob()

    init {
        lifecycle.doOnStart { scope.searchAirports("") }
    }

    override fun invoke(event: AirportsUiEvent) {
        when (event) {
            AirportsUiEvent.Back -> onBack()
            AirportsUiEvent.SnackBarClose -> state.update { it.copy(snackbar = null) }
            AirportsUiEvent.StartUpdate -> scope.onStartUpdate()
            AirportsUiEvent.GetLastUpdate -> scope.onLastUpdate()
            is AirportsUiEvent.SendSearch -> scope.searchAirports(event.search)
            is AirportsUiEvent.ExpandAirport -> scope.expandAirport(event.icao)
        }
    }

    private fun CoroutineScope.onStartUpdate() = launch {
        println("Start update")
        println("Working dir is ${Paths.get("").toAbsolutePath()}")
        state.update { it.copy(updating = true) }
        updateService.updateFlow(this).collect { result ->
            when (result) {
                is AirportUpdateService.UpdateResult.Progress -> {
                    state.update { it.copy(processingFile = result.file, progress = result.count) }
                }

                is AirportUpdateService.UpdateResult.Success -> {
                    val timeString = Calendar.getInstance(Locale.getDefault()).let {
                        it.timeInMillis = result.lastUpdate
                        SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault()).format(it.time)
                    }
                    state.update { it.copy(lastUpdate = timeString) }
                    onStartConvert()
                }

                is AirportUpdateService.UpdateResult.Error -> {
                    state.update { it.copy(snackbar = AirportsSnackBarState.ErrorHint(error = result.message)) }
                }
            }
        }
    }

    private fun CoroutineScope.onStartConvert() = launch {
        csvConverter.convertFiles(this).collect { result ->
            when (result) {
                is LocalBaseConverter.UpdateResult.Progress -> {
                    state.update {
                        it.copy(
                            processingLabel = result.label,
                            airportsCount = result.count
                        )
                    }
                }

                is LocalBaseConverter.UpdateResult.Success -> state.update {
                    launch(Dispatchers.IO) { updateService.clearAfterUpdate() }
                    it.copy(
                        updating = false,
                        airportsCount = result.count
                    )
                }
            }
        }
    }

    private fun CoroutineScope.onLastUpdate() = launch {
        csvConverter.getLastUpdate()?.let { result ->
            val timeString = Calendar.getInstance(Locale.getDefault()).let {
                it.timeInMillis = result.time
                SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault()).format(it.time)
            }
            state.update {
                it.copy(
                    lastUpdate = timeString,
                    airportsCount = result.airports
                )
            }
        }
    }

    private fun CoroutineScope.searchAirports(search: String) = launch {
        state.update { it.copy(searchString = search) }
        val result = airportService.searchAirports(search)
        result.forSuccess { airports ->
            state.update { it.copy(searchResult = airports, snackbar = null) }
        }
        result.forError { type, message ->
            state.update { it.copy(snackbar = AirportsSnackBarState.ErrorHint(message ?: type.name)) }
        }
    }

    private fun CoroutineScope.expandAirport(icao: String) = launch {
        if (state.value.expandedAirport?.icao == icao) {
            state.update { it.copy(expandedAirport = null) }
            return@launch
        }
        val result = airportService.getAirportByICAO(icao)
        result.forSuccess { airport ->
            state.update { it.copy(expandedAirport = airport, snackbar = null) }
        }
        result.forError { type, message ->
            state.update { it.copy(snackbar = AirportsSnackBarState.ErrorHint(message ?: type.name)) }
        }
    }
}