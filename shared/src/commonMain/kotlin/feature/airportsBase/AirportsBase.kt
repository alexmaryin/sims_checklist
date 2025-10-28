package feature.airportsBase

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.airportService.AirportService
import services.airportService.LocalBaseConverter
import services.airportService.updateService.AirportUpdateService
import services.commonApi.forError
import services.commonApi.forSuccess
import utils.pager.Pager
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

class AirportsBase(
    private val componentContext: ComponentContext,
    private val onSelectAirport: (String) -> Unit,
    private val onSelectQfeHelper: (String) -> Unit,
    private val onBack: () -> Unit
) : KoinComponent, AirportEventExecutor, ComponentContext by componentContext {

    private val updateService: AirportUpdateService by inject()
    private val csvConverter: LocalBaseConverter by inject()
    private val airportService: AirportService by inject()

    override val state = MutableValue(AirportsBaseViewState())

    private val pager = Pager(
        initialKey = 0,
        onLoadUpdated = { isLoading -> state.update { it.copy(loadingPage = isLoading) } },
        onRequest = { nextKey ->
            airportService.searchAirports(
                search = state.value.searchString,
                limit = AirportService.SEARCH_LIMIT,
                page = nextKey
            )
        },
        getNextKey = { currentKey, _ -> currentKey + 1 },
        onError = { error ->
            state.update {
                it.copy(snackbar = AirportsSnackBarState.ErrorHint(error.message ?: error.type.name))
            }
        },
        onSuccess = { airports, _ ->
            state.update { currentState ->
                // When a new search starts, searchResult is cleared.
                // The first page of results should replace the empty list, not append to it.
                val newList =
                    if (currentState.searchResult.isEmpty()) airports else currentState.searchResult + airports
                currentState.copy(searchResult = newList)
            }
        },
        endReached = { _, airports -> airports.size < AirportService.SEARCH_LIMIT }
    )

    private val scope = componentContext.coroutineScope() + SupervisorJob()

    init {
        lifecycle.doOnStart {
            scope.onLastUpdate()
            scope.searchAirports("")
        }
    }

    override fun invoke(event: AirportsUiEvent) {
        when (event) {
            AirportsUiEvent.Back -> onBack()
            AirportsUiEvent.SnackBarClose -> state.update { it.copy(snackbar = null) }
            AirportsUiEvent.StartUpdate -> scope.onStartUpdate()
            AirportsUiEvent.GetLastUpdate -> scope.onLastUpdate()
            is AirportsUiEvent.SendSearch ->
                if (event.search != state.value.searchString)
                    scope.searchAirports(event.search)
            AirportsUiEvent.SearchNext -> scope.searchNext()
            is AirportsUiEvent.ExpandAirport -> scope.expandAirport(event.icao)
            is AirportsUiEvent.OpenAirportMetar -> onSelectAirport(event.icao)
            is AirportsUiEvent.OpenQfeHelper -> onSelectQfeHelper(event.icao)
            AirportsUiEvent.TrimList -> state.update {
                pager.reset()
                it.copy(searchResult = state.value.searchResult.take(AirportService.SEARCH_LIMIT))
            }
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
        airportService.getLastUpdate()?.let { result ->
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
        // It's better to clear the list inside the onSuccess callback of the pager
        // to avoid UI flicker. Here we just set the new search string.
        state.update {
            // Clear previous results and set the new search string
            it.copy(searchString = search, searchResult = emptyList())
        }
        pager.reset()
        pager.loadNextItem()
    }

    private fun CoroutineScope.searchNext() = launch {
        pager.loadNextItem()
        println("LIST CAPACITY: ${state.value.searchResult.size}")
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