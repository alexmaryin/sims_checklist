package feature.mainScreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import feature.mainScreen.database.AircraftBase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AircraftRepository
import services.aircraftService.removeCustomAircraftFiles
import services.airportService.AirportService
import services.airportService.LocalBaseConverter
import services.airportService.updateService.AirportUpdateService
import services.storage.aircraftDir

class AircraftList(
    private val componentContext: ComponentContext,
    private val onUiCommand: (event: MainScreenEvent) -> Unit,
) : MainEventExecutor, KoinComponent, ComponentContext by componentContext {

    private val aircraftBase: AircraftBase by inject()
    private val aircraftRepository: AircraftRepository by inject()
    private val airportService: AirportService by inject()
    private val updateService: AirportUpdateService by inject()
    private val csvConverter: LocalBaseConverter by inject()

    private val scope = componentContext.coroutineScope() + SupervisorJob()
    override val state = MutableValue(MainScreenViewState())

    init {
        lifecycle.doOnCreate {
            state.update { it.copy(isLoading = true) }
        }

        // Reload on every start (including resume after returning from the import screen)
        // so newly added custom aircraft appear without an app restart.
        lifecycle.doOnStart {
            scope.launch {
                aircraftBase.getAircraft()?.let { base ->
                    state.update { it.copy(aircraftList = base, isLoading = false) }
                }
                val warnings = aircraftBase.loadWarnings
                if (warnings.isNotEmpty()) {
                    state.update { it.copy(snack = MainScreenSnack.Close(warnings.joinToString("; "))) }
                }
            }
        }

        lifecycle.doOnStart(true) {
            scope.launch {
                val count: Long?
                val snack: MainScreenSnack?
                if (airportService.isEmpty()) {
                    count = null
                    snack = MainScreenSnack.StartUpdate("Airport database is empty!")
                } else {
                    val last = airportService.getLastUpdate()
                    count = last?.airports
                    snack = null
                }
                state.update { it.copy(snack = snack, airportsCount = count) }
            }
        }

        lifecycle.doOnStop { state.update { it.copy(snack = null, updateMessage = null, progress = 0f) } }
    }

    override fun invoke(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.ClearSnack -> state.update { it.copy(snack = null) }
            MainScreenEvent.StartUpdate -> scope.onStartUpdate()
            MainScreenEvent.DropBaseConfirm -> scope.onConfirmDropBase()
            MainScreenEvent.DropBase -> scope.onDropBase()
            MainScreenEvent.OpenToolsMenu -> scope.onRefreshAirportsCount()
            is MainScreenEvent.RemoveAircraftConfirm -> scope.onRemoveAircraftConfirm(event.aircraftId)
            is MainScreenEvent.RemoveAircraft -> scope.onRemoveAircraft(event.aircraftId)
            else -> onUiCommand(event)
        }
    }

    private fun CoroutineScope.onStartUpdate() = launch {
        var downloadSucceeded = false
        try {
            updateService.updateFlow(this).collect {
                when (it) {
                    is AirportUpdateService.UpdateResult.Progress -> {
                        state.update { old -> old.copy(
                            updateMessage = "Downloading ${it.file}",
                            progress = it.count.toFloat()
                        ) }
                    }

                    is AirportUpdateService.UpdateResult.Error -> {
                        state.update { old -> old.copy(
                            updateMessage = null,
                            progress = 0f,
                            snack = MainScreenSnack.Close("Some files are missing. Try again later.")
                        ) }
                    }

                    is AirportUpdateService.UpdateResult.Success -> {
                        downloadSucceeded = true
                    }
                }
            }
            if (!downloadSucceeded) {
                state.update { old -> old.copy(updateMessage = null, progress = 0f) }
                return@launch
            }
            csvConverter.convertFiles(this).collect {
                when (it) {
                    is LocalBaseConverter.UpdateResult.Progress -> {
                        state.update { old -> old.copy(updateMessage = "Converting ${it.label}") }
                    }

                    is LocalBaseConverter.UpdateResult.Success -> {
                        state.update { old ->
                            old.copy(
                                updateMessage = null,
                                progress = 0f,
                                airportsCount = it.count,
                                snack = MainScreenSnack.Close("Updated successfully. ${it.count} airports in database.")
                            )
                        }
                    }
                }
            }
            updateService.clearAfterUpdate()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            state.update { old -> old.copy(
                updateMessage = null,
                progress = 0f,
                snack = MainScreenSnack.Close(e.message ?: "Update failed")
            ) }
        }
    }

    private fun CoroutineScope.onConfirmDropBase() = launch {
        state.update { it.copy(snack = MainScreenSnack.DropBase("Are you sure?")) }
    }
    private fun CoroutineScope.onDropBase() = launch {
        airportService.dropAll()
        state.update {
            it.copy(
                airportsCount = 0,
                snack = MainScreenSnack.StartUpdate("Database dropped. Download again?")
            )
        }
    }

    private fun CoroutineScope.onRefreshAirportsCount() = launch {
        val count = airportService.getLastUpdate()?.airports
        state.update { it.copy(airportsCount = count) }
    }

    private fun CoroutineScope.onRemoveAircraftConfirm(aircraftId: Int) = launch {
        val name = state.value.aircraftList.firstOrNull { it.id == aircraftId }?.name ?: "this aircraft"
        state.update {
            it.copy(snack = MainScreenSnack.RemoveAircraft(aircraftId, "Remove $name?"))
        }
    }

    private fun CoroutineScope.onRemoveAircraft(aircraftId: Int) = launch {
        withContext(Dispatchers.IO) { removeCustomAircraftFiles(aircraftDir(), aircraftId) }
        aircraftRepository.removeAircraft(aircraftId)
        state.update {
            it.copy(
                aircraftList = it.aircraftList.filterNot { aircraft -> aircraft.id == aircraftId },
                snack = null
            )
        }
    }
}